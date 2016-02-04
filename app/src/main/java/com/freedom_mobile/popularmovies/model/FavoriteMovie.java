package com.freedom_mobile.popularmovies.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FavoriteMovie")
public class FavoriteMovie extends ParseObject implements Parcelable {

    // Parcel keys
    private static final String KEY_ID = "id";
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_MOVIE_POSTER = "movie_poster";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_RATING = "rating";

    public static final Creator<FavoriteMovie> CREATOR = new Creator<FavoriteMovie>() {
        @Override
        public FavoriteMovie createFromParcel(Parcel source) {
            // Read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // Instantiate a movie using values from the bundle
            return new FavoriteMovie(
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
        public FavoriteMovie[] newArray(int size) {
            return new FavoriteMovie[size];
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

    public FavoriteMovie() {
        super();
        // Empty constructor.
    }

    public FavoriteMovie(String id, int movieId, String title, String overview,
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
        return getString("id");
    }

    public void setId(String id) {
        put("id", id);
    }

    public int getMovieId() {
        return getInt("movieId");
    }

    public void setMovieId(int movieId) {
        put("movieId", movieId);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getOverview() {
        return getString("overview");
    }

    public void setOverview(String overview) {
        put("overview", overview);
    }

    public String getReleaseDate() {
        return getString("releaseDate");
    }

    public void setReleaseDate(String releaseDate) {
        put("releaseDate", releaseDate);
    }

    public String getMoviePoster() {
        return getString("moviePoster");
    }

    public void setMoviePoster(String moviePoster) {
        put("moviePoster", moviePoster);
    }

    public double getPopularity() {
        return getDouble("popularity");
    }

    public void setPopularity(double popularity) {
        put("popularity", popularity);
    }

    public double getRating() {
        return getDouble("rating");
    }

    public void setRating(double rating) {
        put("rating", rating);
    }

    public ParseUser getUser()  {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
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
