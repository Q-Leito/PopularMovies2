package com.freedom_mobile.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Movie implements Parcelable {

    // Parcel keys
    private static final String KEY_ID = "id";
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_POSTER = "movie_poster";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_RATING = "rating";

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            // Read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // Instantiate a movie using values from the bundle
            return new Movie(
                    bundle.getString(KEY_ID),
                    bundle.getInt(KEY_MOVIE_ID),
                    bundle.getString(KEY_TITLE),
                    bundle.getString(KEY_OVERVIEW),
                    bundle.getString(KEY_RELEASE_DATE),
                    bundle.getString(KEY_MOVIE_POSTER),
                    bundle.getDouble(KEY_POPULARITY),
                    bundle.getDouble(KEY_RATING)
            );
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private String mId;
    private int mMovieId;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mMoviePoster;
    private double mPopularity;
    private double mRating;
    public static HashMap<String, Movie> mPopularMovieList = new HashMap<>();
    public static HashMap<String, Movie> mHighestRatedMovieList = new HashMap<>();

    public Movie() {
        super();
        // Empty constructor.
    }

    public Movie(String id, int movieId, String title, String overview,
                 String releaseDate, String moviePoster, double popularity,
                 double rating) {
        super();
        mId = id;
        mMovieId = movieId;
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mMoviePoster = moviePoster;
        mPopularity = popularity;
        mRating = rating;

        setId(id);
        setMovieId(movieId);
        setTitle(title);
        setOverview(overview);
        setReleaseDate(releaseDate);
        setMoviePoster(moviePoster);
        setPopularity(popularity);
        setRating(rating);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        mMoviePoster = moviePoster;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public void addPopularMovie(Movie movieFromTMDB) {
        if (!mPopularMovieList.containsKey(movieFromTMDB.getId())) {
            mPopularMovieList.put(movieFromTMDB.getId(), movieFromTMDB);
        }
    }

    public void addHighestRatedMovie(Movie movieFromTMDB) {
        if (!mHighestRatedMovieList.containsKey(movieFromTMDB.getId())) {
            mHighestRatedMovieList.put(movieFromTMDB.getId(), movieFromTMDB);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // create a bundle for the key value pairs
        Bundle bundle = new Bundle();

        // Insert the key value pairs to the bundle
        bundle.putString(KEY_ID, mId);
        bundle.putInt(KEY_MOVIE_ID, mMovieId);
        bundle.putString(KEY_TITLE, mTitle);
        bundle.putString(KEY_OVERVIEW, mOverview);
        bundle.putString(KEY_RELEASE_DATE, mReleaseDate);
        bundle.putString(KEY_MOVIE_POSTER, mMoviePoster);
        bundle.putDouble(KEY_POPULARITY, mPopularity);
        bundle.putDouble(KEY_RATING, mRating);

        dest.writeBundle(bundle);
    }
}
