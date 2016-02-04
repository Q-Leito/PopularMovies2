package com.freedom_mobile.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.model.Movie;
import com.freedom_mobile.popularmovies.utils.Urls;
import com.freedom_mobile.popularmovies.utils.Utils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();
    public static final String KEY_MOVIE_LIST = "movies";
    public static final String LIST_STATE = "list_state";
    public static final String MOVIE_DATA = "movie_data";

    private Callbacks mCallbacks;
    private Movie mMovie;
    private Utils mUtils;

    @Bind(R.id.popularMoviesRecyclerView) RecyclerView mRecyclerView;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks extends Utils.Callbacks {
        /**
         * Callback for when a movie has been selected.
         */
        void onMovieSelected(String id);
    }

    public MainFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Read the movie list from the saved state
            Movie.mPopularMovieList = (HashMap<String, Movie>) savedInstanceState.getSerializable(KEY_MOVIE_LIST);
            mMovie = savedInstanceState.getParcelable(MOVIE_DATA);
        }

        if (getActivity() != null) {
            mUtils = new Utils(getActivity());
            mUtils.getMovieData(Urls.SORT_BY_POPULARITY_DESC,
                    mMovie,
                    TAG,
                    Movie.mPopularMovieList,
                    mRecyclerView,
                    mCallbacks);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        int recyclerState = mRecyclerView.getScrollState();
        state.putInt(LIST_STATE, recyclerState);
        state.putSerializable(KEY_MOVIE_LIST, Movie.mPopularMovieList);
        state.putParcelable(MOVIE_DATA, mMovie);
    }
}
