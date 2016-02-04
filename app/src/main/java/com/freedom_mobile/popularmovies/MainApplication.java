package com.freedom_mobile.popularmovies;

import android.app.Application;

import com.freedom_mobile.popularmovies.model.FavoriteMovie;
import com.freedom_mobile.popularmovies.utils.ApiKeys;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models.
        ParseObject.registerSubclass(FavoriteMovie.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, ApiKeys.PARSE_APPLICATION_ID, ApiKeys.PARSE_CLIENT_KEY);
    }

    public static void updateParseInstallation(ParseUser parseUser) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("userId", parseUser.getObjectId());
        installation.saveInBackground();
    }
}
