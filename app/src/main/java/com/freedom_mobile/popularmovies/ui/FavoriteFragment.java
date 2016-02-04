package com.freedom_mobile.popularmovies.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.adapters.MovieAdapter;
import com.freedom_mobile.popularmovies.model.FavoriteMovie;
import com.freedom_mobile.popularmovies.model.Review;
import com.freedom_mobile.popularmovies.model.Trailer;
import com.freedom_mobile.popularmovies.utils.RecyclerTouchListener;
import com.freedom_mobile.popularmovies.utils.SpacesItemDecoration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment {
    public static final String TAG = FavoriteFragment.class.getSimpleName();
    public static final int PORTRAIT_MODE = 2;
    public static final int LANDSCAPE_TABLET_MODE = 3;
    public static final int LANDSCAPE_MODE = 4;

    private MovieAdapter mMovieAdapter;
    private Callbacks mCallbacks;

    @Bind(R.id.favoriteRecyclerView) RecyclerView mRecyclerView;
    private LinkedHashMap<String, FavoriteMovie> mFavoriteMovieList;

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

    public FavoriteFragment() {
        // Required constructor.
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, rootView);

        getFavoriteMovies();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Trailer.mTrailerList.clear();
                        Review.mReviewList.clear();
                        mCallbacks.onMovieSelected(String.valueOf(mFavoriteMovieList.get(
                                String.valueOf(position)).getMovieId()));
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        float recyclerViewSpacing = getResources().getDimension(R.dimen.recyclerViewPadding);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) recyclerViewSpacing));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavoriteMovies();
    }

    private void getFavoriteMovies() {
        if (isAdded() && getActivity() != null) {
            ParseQuery<FavoriteMovie> query = ParseQuery.getQuery("FavoriteMovie");

            query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
            query.addDescendingOrder("createdAt");
            query.findInBackground(new FindCallback<FavoriteMovie>() {
                @Override
                public void done(List<FavoriteMovie> favoriteMovieList, ParseException e) {
                    if (e == null) {
                        if (favoriteMovieList.size() == 0) {
                            // Do nothing!
                        } else {
                            mFavoriteMovieList = new LinkedHashMap<>();
                            int i = 0;
                            for (FavoriteMovie favoriteMovie : favoriteMovieList) {
                                mFavoriteMovieList.put(String.valueOf(i), favoriteMovie);
                                i++;
                            }

                            mRecyclerView.setHasFixedSize(true);
                            if (getActivity() != null) {
                                if (getActivity().getResources().getConfiguration().orientation
                                        == Configuration.ORIENTATION_PORTRAIT) {
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), PORTRAIT_MODE));
                                } else if (getActivity().getResources().getConfiguration().orientation
                                        == Configuration.ORIENTATION_LANDSCAPE && getResources().getBoolean(R.bool.isTablet)) {
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), LANDSCAPE_TABLET_MODE));
                                } else if (getActivity().getResources().getConfiguration().orientation
                                        == Configuration.ORIENTATION_LANDSCAPE && !getResources().getBoolean(R.bool.isTablet)) {
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), LANDSCAPE_MODE));
                                }
                            }

                            mMovieAdapter = new MovieAdapter(getActivity(), mFavoriteMovieList);
                            mRecyclerView.setAdapter(mMovieAdapter);

                        }
                    }
                }
            });
        }
    }
}
