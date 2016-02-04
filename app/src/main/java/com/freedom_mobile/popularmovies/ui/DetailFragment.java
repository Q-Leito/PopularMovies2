package com.freedom_mobile.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.adapters.ReviewAdapter;
import com.freedom_mobile.popularmovies.adapters.TrailerAdapter;
import com.freedom_mobile.popularmovies.model.FavoriteMovie;
import com.freedom_mobile.popularmovies.model.Movie;
import com.freedom_mobile.popularmovies.model.Review;
import com.freedom_mobile.popularmovies.model.Trailer;
import com.freedom_mobile.popularmovies.utils.ApiKeys;
import com.freedom_mobile.popularmovies.utils.RecyclerTouchListener;
import com.freedom_mobile.popularmovies.utils.Urls;
import com.freedom_mobile.popularmovies.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";
    public static final int TRAILER_ITEM_SIZE = 355;
    public static final int REVIEW_ITEM_SIZE = 600;
    private static final int TABLET_TRAILER_ITEM_SIZE = 105;
    private static final int TABLET_REVIEW_ITEM_SIZE = 1100;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private View mRootView;
    private Movie mMovie;
    private FavoriteMovie mFavoriteMovie;
    private Trailer mTrailer;
    private Review mReview;
    private Utils mUtils;

    @Bind(R.id.movie_poster) ImageView mMoviePoster;
    @Bind(R.id.movie_title_label) TextView mMovieTitleLabel;
    @Bind(R.id.movie_date_status) TextView mMovieDateStatusLabel;
    @Bind(R.id.movie_rating) TextView mMovieRatingLabel;
    @Bind(R.id.add_to_favorite_button) Button mAddToFavoriteButton;
    @Bind(R.id.movie_overview) TextView mMovieOverviewLabel;
    @Bind(R.id.trailer_recycler_view) RecyclerView mTrailerRecyclerView;
    @Bind(R.id.review_recycler_view) RecyclerView mReviewRecyclerView;
    private CollapsingToolbarLayout mAppBarLayout;
    private List<FavoriteMovie> mFavoriteMovies;

    public DetailFragment() {
        // Required constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE_ID)) {
            if (getArguments().containsKey(MainFragment.TAG)) {
                mMovie = Movie.mPopularMovieList.get(getArguments().getString(MOVIE_ID));
            } else if (getArguments().containsKey(HighestRatedFragment.TAG)){
                mMovie = Movie.mHighestRatedMovieList.get(getArguments().getString(MOVIE_ID));
            }

            Activity activity = this.getActivity();
            mAppBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (mAppBarLayout != null) {
                if (mMovie != null) {
                    mAppBarLayout.setTitle(mMovie.getTitle());
                }
            }
        }

        if (getActivity() != null) {
            mUtils = new Utils(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, mRootView);

        if (mMovie != null) {
            ParseQuery<FavoriteMovie> query = ParseQuery.getQuery("FavoriteMovie");

            query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
            query.findInBackground(new FindCallback<FavoriteMovie>() {
                @Override
                public void done(List<FavoriteMovie> favoriteMovieList, ParseException e) {
                    if (e == null) {
                        if (favoriteMovieList.size() == 0) {
                            mAddToFavoriteButton.setText(getString(R.string.add_to_favorite));
                            addToFavoriteButton();
                            return;
                        }

                        for (final FavoriteMovie favoriteMovie : favoriteMovieList) {
                            if (favoriteMovie.getTitle().equals(mMovie.getTitle())) {
                                mAddToFavoriteButton.setText(getString(R.string.delete_from_favorite));
                                if (mAddToFavoriteButton.getText() == getString(R.string.delete_from_favorite)){
                                    removeFromFavoriteButton(favoriteMovie);
                                } else {
                                    addToFavoriteButton();
                                }
                                return;
                            } else {
                                mAddToFavoriteButton.setText(getString(R.string.add_to_favorite));
                                if (mAddToFavoriteButton.getText() == getString(R.string.add_to_favorite)) {
                                    addToFavoriteButton();
                                } else {
                                    removeFromFavoriteButton(favoriteMovie);
                                }
                            }
                        }
                    }
                }
            });

            int movieId = mMovie.getMovieId();
            String moviePoster = mMovie.getMoviePoster();
            String movieTitle = mMovie.getTitle();
            String movieOverview = mMovie.getOverview();
            String movieReleaseDate = mMovie.getReleaseDate();
            double movieRating = mMovie.getRating();

            Picasso.with(getActivity())
                    .load(Urls.MOVIE_POSTER_URL + moviePoster)
                    .into(mMoviePoster);

            mMovieTitleLabel.setText(movieTitle);
            mMovieOverviewLabel.setText(movieOverview);
            mMovieDateStatusLabel.setText(movieReleaseDate);
            mMovieRatingLabel.setText(String.format("%s%s",
                    String.valueOf(movieRating), getString(R.string.highest_rating)));

            getTrailerData(movieId);
            getReviewData(movieId);
        } else {
            if (getArguments().containsKey(MOVIE_ID)) {
                if (getArguments().containsKey(FavoriteFragment.TAG)) {
                    ParseQuery<FavoriteMovie> query = ParseQuery.getQuery("FavoriteMovie");

                    query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
                    query.whereEqualTo("movieId", Integer.parseInt(getArguments().getString(MOVIE_ID)));
                    query.findInBackground(new FindCallback<FavoriteMovie>() {
                        @Override
                        public void done(List<FavoriteMovie> favoriteMovieList, ParseException e) {
                            if (e == null) {
                                for (final FavoriteMovie favoriteMovie : favoriteMovieList) {
                                    if (!getResources().getBoolean(R.bool.isTablet)) {
                                        mAppBarLayout.setTitle(favoriteMovie.getTitle());
                                    }
                                    int movieId = favoriteMovie.getMovieId();
                                    String moviePoster = favoriteMovie.getMoviePoster();
                                    String movieTitle = favoriteMovie.getTitle();
                                    String movieOverview = favoriteMovie.getOverview();
                                    String movieReleaseDate = favoriteMovie.getReleaseDate();
                                    double movieRating = favoriteMovie.getRating();

                                    mFavoriteMovies = new ArrayList<>();
                                    mFavoriteMovies.add(favoriteMovie);

                                    mAddToFavoriteButton.setText(getString(R.string.delete_from_favorite));
                                    if (mAddToFavoriteButton.getText() == getString(R.string.delete_from_favorite)) {
                                        removeFromFavoriteButton(favoriteMovie);
                                    }

                                    Picasso.with(getActivity())
                                            .load(Urls.MOVIE_POSTER_URL + moviePoster)
                                            .into(mMoviePoster);

                                    mMovieTitleLabel.setText(movieTitle);
                                    mMovieOverviewLabel.setText(movieOverview);
                                    mMovieDateStatusLabel.setText(movieReleaseDate);
                                    mMovieRatingLabel.setText(String.format("%s%s",
                                            String.valueOf(movieRating), getString(R.string.highest_rating)));

                                    getTrailerData(movieId);
                                    getReviewData(movieId);
                                }
                            }
                        }
                    });
                }
            }
        }

        return mRootView;
    }

    private void refreshFragment() {
        Fragment currentFragment = (getActivity()).getSupportFragmentManager()
                .findFragmentByTag(TAG);
        if (currentFragment instanceof DetailFragment) {
            FragmentTransaction fragmentTransaction =
                    getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment).attach(currentFragment)
                    .commit();
        }
    }

    private void removeFromFavoriteButton(final FavoriteMovie favoriteMovie) {
        mAddToFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getArguments().containsKey(FavoriteFragment.TAG)) {
                    favoriteMovie.deleteInBackground();
                    mAddToFavoriteButton.setText(getString(R.string.add_to_favorite));
                    Snackbar.make(view, "Movie deleted from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    refreshFragment();
                } else {
                    if (mAddToFavoriteButton.getText() == getString(R.string.delete_from_favorite)) {
                        favoriteMovie.deleteInBackground();
                        mAddToFavoriteButton.setText(getString(R.string.add_to_favorite));
                        Snackbar.make(view, "Movie deleted from favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        addToFavorite();
                        mAddToFavoriteButton.setText(getString(R.string.delete_from_favorite));
                        Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    private void addToFavoriteButton() {
        mAddToFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getArguments().containsKey(FavoriteFragment.TAG)) {
                    addToFavorite();
                    mAddToFavoriteButton.setText(getString(R.string.delete_from_favorite));
                    Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    refreshFragment();
                } else {
                    addToFavorite();
                    mAddToFavoriteButton.setText(getString(R.string.delete_from_favorite));
                    Snackbar.make(view, "Movie added to favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void addToFavorite() {
        if (mMovie != null) {
            mFavoriteMovie = new FavoriteMovie(
                    ParseUser.getCurrentUser().getObjectId(),
                    mMovie.getMovieId(),
                    mMovie.getTitle(),
                    mMovie.getOverview(),
                    mMovie.getReleaseDate(),
                    mMovie.getMoviePoster(),
                    mMovie.getPopularity(),
                    mMovie.getRating()
            );
            mFavoriteMovie.setOwner(ParseUser.getCurrentUser());
            mFavoriteMovie.saveInBackground();
        } else {
            mFavoriteMovie = new FavoriteMovie(
                    ParseUser.getCurrentUser().getObjectId(),
                    mFavoriteMovies.get(0).getMovieId(),
                    mFavoriteMovies.get(0).getTitle(),
                    mFavoriteMovies.get(0).getOverview(),
                    mFavoriteMovies.get(0).getReleaseDate(),
                    mFavoriteMovies.get(0).getMoviePoster(),
                    mFavoriteMovies.get(0).getPopularity(),
                    mFavoriteMovies.get(0).getRating()
            );
            mFavoriteMovie.setOwner(ParseUser.getCurrentUser());
            mFavoriteMovie.saveInBackground();
        }
    }

    private void getTrailerData(int movieId) {
        String trailerData = Urls.MOVIE_URL + movieId + "/videos?" + ApiKeys.TMDB_API_KEY;

        if (mUtils.isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(trailerData)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    mUtils.alertAboutError();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mTrailer = getTrailerDetails(jsonData);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateDisplay();
                                    }
                                });
                            }
                        } else {
                            mUtils.alertAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Snackbar.make(mRootView.findViewById(R.id.movie_detail),
                    getString(R.string.network_unavailable),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void getReviewData(int movieId) {
        String reviewData = Urls.MOVIE_URL + movieId + "/reviews?" + ApiKeys.TMDB_API_KEY;

        if (mUtils.isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(reviewData)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    mUtils.alertAboutError();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
//                        Log.i(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mReview = getReviewDetails(jsonData);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateDisplay();
                                    }
                                });
                            }
                        } else {
                            mUtils.alertAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Snackbar.make(mRootView.findViewById(R.id.movie_detail),
                    getString(R.string.network_unavailable),
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private Trailer getTrailerDetails(String jsonData)
            throws JSONException {
        JSONObject trailerData = new JSONObject(jsonData);
        JSONArray results = trailerData.getJSONArray("results");

        mTrailer = new Trailer();
        for (int i = 0; i < results.length(); i++) {
            JSONObject resultsArray = results.getJSONObject(i);

            mTrailer.addTrailer(new Trailer(
                    String.valueOf(i),
                    resultsArray.getString("id"),
                    resultsArray.getString("key"),
                    resultsArray.getString("name"),
                    resultsArray.getString("site"),
                    resultsArray.getInt("size"),
                    resultsArray.getString("type")
            ));
        }

        return mTrailer;
    }

    private Review getReviewDetails(String jsonData)
            throws JSONException {
        JSONObject reviewData = new JSONObject(jsonData);
        JSONArray results = reviewData.getJSONArray("results");

        mReview = new Review();
        for (int i = 0; i < results.length(); i++) {
            JSONObject resultsArray = results.getJSONObject(i);

            mReview.addReview(new Review(
                    String.valueOf(i),
                    resultsArray.getString("id"),
                    resultsArray.getString("author"),
                    resultsArray.getString("content")
            ));
        }

        return mReview;
    }

    private void updateDisplay() {
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setLayoutManager(new GridLayoutManager(
                getActivity(), 1));
        mTrailerAdapter = new TrailerAdapter(getActivity(), Trailer.mTrailerList);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            mTrailerRecyclerView.getLayoutParams().height = TRAILER_ITEM_SIZE
                    * mTrailerAdapter.getItemCount();
        } else {
            mTrailerRecyclerView.getLayoutParams().height = TABLET_TRAILER_ITEM_SIZE
                    * mTrailerAdapter.getItemCount();
        }
        mTrailerAdapter.notifyDataSetChanged();
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mTrailerRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String trailerKey = Trailer.mTrailerList.get(String.valueOf(position)).getKey();
                String trailerUrl = Urls.YOUTUBE_URL + trailerKey;
                Uri uri = Uri.parse(trailerUrl);
                uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setLayoutManager(new GridLayoutManager(
                getActivity(), 1));
        mReviewAdapter = new ReviewAdapter(Review.mReviewList);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            mReviewRecyclerView.getLayoutParams().height = REVIEW_ITEM_SIZE
                    * mReviewAdapter.getItemCount();
        } else {
            mReviewRecyclerView.getLayoutParams().height = TABLET_REVIEW_ITEM_SIZE
                    * mReviewAdapter.getItemCount();
        }
        mReviewAdapter.notifyDataSetChanged();
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }
}
