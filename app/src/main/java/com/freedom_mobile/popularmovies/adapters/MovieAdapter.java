package com.freedom_mobile.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.model.FavoriteMovie;
import com.freedom_mobile.popularmovies.model.Movie;
import com.freedom_mobile.popularmovies.utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private HashMap<String, Movie> mMovies;
    private HashMap<String, FavoriteMovie> mFavoriteMovies;
    private Map<String, ?> mMap;

    public MovieAdapter(Context context, HashMap<String, Movie> movies) {
        mContext = context;
        mMovies = movies;
        mMap = movies;
    }

    public MovieAdapter(Context context, LinkedHashMap<String, FavoriteMovie> favoriteMovies) {
        mContext = context;
        mFavoriteMovies = favoriteMovies;
        mMap = favoriteMovies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_poster, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (mMap == mMovies) {
            String moviePoster = mMovies.get(String.valueOf(position)).getMoviePoster();
            String movieTitle = mMovies.get(String.valueOf(position)).getTitle();

            Picasso.with(mContext)
                    .load(Urls.MOVIE_POSTER_URL + moviePoster)
                    .into(viewHolder.mMoviePoster);

            viewHolder.mMoviePosterLabel.setText(movieTitle);
        } else {
            String moviePoster = mFavoriteMovies.get(String.valueOf(position)).getMoviePoster();
            String movieTitle = mFavoriteMovies.get(String.valueOf(position)).getTitle();

            Picasso.with(mContext)
                    .load(Urls.MOVIE_POSTER_URL + moviePoster)
                    .into(viewHolder.mMoviePoster);

            viewHolder.mMoviePosterLabel.setText(movieTitle);
        }
    }

    @Override
    public int getItemCount() {
        return mMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movie_poster) ImageView mMoviePoster;
        @Bind(R.id.movie_poster_label) TextView mMoviePosterLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
