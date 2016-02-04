package com.freedom_mobile.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.adapters.MovieAdapter;
import com.freedom_mobile.popularmovies.model.Movie;
import com.freedom_mobile.popularmovies.model.Review;
import com.freedom_mobile.popularmovies.model.Trailer;
import com.freedom_mobile.popularmovies.ui.HighestRatedFragment;
import com.freedom_mobile.popularmovies.ui.MainFragment;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final int PORTRAIT_MODE = 2;
    public static final int LANDSCAPE_TABLET_MODE = 3;
    public static final int LANDSCAPE_MODE = 4;

    private Context mContext;

    public Utils(Context context) {
        mContext = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public void alertAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        if (mContext != null) {
            alertDialogFragment.show(((AppCompatActivity) mContext)
                    .getFragmentManager(), "error_dialog");
        }
    }

    public void getMovieData(String sortBy, final Movie movie, final String tag,
                             final HashMap hashMap, final RecyclerView recyclerView,
                             final Callbacks callbacks) {
        String movieUrl = Urls.DISCOVER_URL
                + sortBy
                + ApiKeys.TMDB_API_KEY;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(movieUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    alertAboutError();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            if (movie == null) {
                                getMovieDetails(jsonData, tag);
                            }
                            if (mContext != null) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateDisplay(recyclerView, callbacks, hashMap);
                                    }
                                });
                            }
                        } else {
                            alertAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.network_unavailable),
                    Toast.LENGTH_LONG).show();
        }
    }

    private Movie getMovieDetails(String jsonData, String tag)
            throws JSONException {
        JSONObject moviesData = new JSONObject(jsonData);

        JSONArray results = moviesData.getJSONArray("results");

        Movie movie = new Movie();
        for (int i = 0; i < results.length(); i++) {
            JSONObject resultsArray = results.getJSONObject(i);

            if (tag.equals(MainFragment.TAG)) {
                movie.addPopularMovie(new Movie(
                        String.valueOf(i),
                        resultsArray.getInt("id"),
                        resultsArray.getString("original_title"),
                        resultsArray.getString("overview"),
                        resultsArray.getString("release_date"),
                        resultsArray.getString("poster_path"),
                        resultsArray.getDouble("popularity"),
                        resultsArray.getDouble("vote_average")
                ));
            } else if (tag.equals(HighestRatedFragment.TAG)) {
                movie.addHighestRatedMovie(new Movie(
                    String.valueOf(i),
                    resultsArray.getInt("id"),
                    resultsArray.getString("original_title"),
                    resultsArray.getString("overview"),
                    resultsArray.getString("release_date"),
                    resultsArray.getString("poster_path"),
                    resultsArray.getDouble("popularity"),
                    resultsArray.getDouble("vote_average")
                ));
            }
        }

        return movie;
    }

    private void updateDisplay(RecyclerView recyclerView, final Callbacks callbacks, HashMap hashMap) {
        float recyclerViewSpacing = mContext.getResources().getDimension(R.dimen.recyclerViewPadding);

        recyclerView.addItemDecoration(new SpacesItemDecoration((int) recyclerViewSpacing));
        recyclerView.setHasFixedSize(true);
        if (mContext != null) {
            if (mContext.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, PORTRAIT_MODE));
            } else if (mContext.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE && mContext.getResources().getBoolean(R.bool.isTablet)) {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, LANDSCAPE_TABLET_MODE));
            } else if (mContext.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE && !mContext.getResources().getBoolean(R.bool.isTablet)) {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, LANDSCAPE_MODE));
            }
        }
        recyclerView.setAdapter(new MovieAdapter(mContext, hashMap));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Trailer.mTrailerList.clear();
                        Review.mReviewList.clear();
                        callbacks.onMovieSelected(String.valueOf(position));
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when a movie has been selected.
         */
        void onMovieSelected(String id);
    }
}
