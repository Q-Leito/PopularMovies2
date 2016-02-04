package com.freedom_mobile.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.freedom_mobile.popularmovies.MainApplication;
import com.freedom_mobile.popularmovies.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.Callbacks, HighestRatedFragment.Callbacks,
        FavoriteFragment.Callbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    private boolean mTwoPane;
    private HighestRatedFragment mHighestRatedFragment;
    private FavoriteFragment mFavoriteFragment;
    private MainFragment mMainFragment;
    protected int mItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = getToolbar();

        getNavigationDrawer(toolbar);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            ParseUser newUser = new ParseUser();
            String randomToString = new Random().toString();
            String uuid = UUID.randomUUID().toString();
            newUser.setUsername(randomToString);
            newUser.setPassword(randomToString);
            newUser.setEmail(uuid + "@example.com");
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Successfully saved the new user!
                        MainApplication.updateParseInstallation(
                                ParseUser.getCurrentUser());
                    }
                }
            });
        } else {
            Log.i(TAG, currentUser.getUsername());
        }

        mMainFragment = new MainFragment();
        mHighestRatedFragment = new HighestRatedFragment();
        mFavoriteFragment = new FavoriteFragment();

        if (mItemId == 0) {
            startFragmentTransaction(mMainFragment);
        }

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private Toolbar getToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void getNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mItemId = item.getItemId();

        switch (mItemId) {
            case R.id.nav_popular:
                startFragmentTransaction(mMainFragment);
                break;
            case R.id.nav_highest_rated:
                startFragmentTransaction(mHighestRatedFragment);
                break;
            case R.id.nav_favorite:
                startFragmentTransaction(mFavoriteFragment);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ITEM_ID, mItemId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mItemId = savedInstanceState.getInt(KEY_ITEM_ID);

        switch (mItemId) {
            case R.id.nav_popular:
                startFragmentTransaction(mMainFragment);
                break;
            case R.id.nav_highest_rated:
                startFragmentTransaction(mHighestRatedFragment);
                break;
            case R.id.nav_favorite:
                startFragmentTransaction(mFavoriteFragment);
                break;
        }
    }

    /**
     * Callback method from {@link MainFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onMovieSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (mItemId == R.id.nav_popular || mItemId == 0) {
                startFragmentTransactionWithBundle(id, MainFragment.TAG);
            } else if (mItemId == R.id.nav_highest_rated) {
                startFragmentTransactionWithBundle(id, HighestRatedFragment.TAG);
            } else {
                startFragmentTransactionWithBundle(id, FavoriteFragment.TAG);
            }
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            if (mItemId == R.id.nav_popular || mItemId == 0) {
                startIntentWithExtra(id, MainFragment.TAG);
            } else if (mItemId == R.id.nav_highest_rated) {
                startIntentWithExtra(id, HighestRatedFragment.TAG);
            } else {
                startIntentWithExtra(id, FavoriteFragment.TAG);
            }
        }
    }

    private void startFragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void startIntentWithExtra(String id, String tag) {
        Intent movieDetailsIntent = new Intent(this, DetailActivity.class);
        movieDetailsIntent.putExtra(DetailFragment.MOVIE_ID, id);
        movieDetailsIntent.putExtra(tag, tag);
        startActivity(movieDetailsIntent);
    }

    private void startFragmentTransactionWithBundle(String id, String tag) {
        Bundle arguments = new Bundle();
        arguments.putString(DetailFragment.MOVIE_ID, id);
        arguments.putString(tag, tag);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, detailFragment,
                        DetailFragment.TAG).commit();
    }
}
