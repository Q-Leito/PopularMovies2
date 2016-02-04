package com.freedom_mobile.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.freedom_mobile.popularmovies.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            if (getIntent().hasExtra(MainFragment.TAG)) {
                startFragmentTransaction(MainFragment.TAG, getIntent().getStringExtra(MainFragment.TAG));
            } else if (getIntent().hasExtra(HighestRatedFragment.TAG)) {
                startFragmentTransaction(HighestRatedFragment.TAG, getIntent().getStringExtra(HighestRatedFragment.TAG));
            } else {
                startFragmentTransaction(FavoriteFragment.TAG, getIntent().getStringExtra(FavoriteFragment.TAG));
            }
        }
    }

    private void startFragmentTransaction(String tag, String stringValue) {
        Bundle arguments = new Bundle();
        arguments.putString(DetailFragment.MOVIE_ID,
                getIntent().getStringExtra(DetailFragment.MOVIE_ID));
        arguments.putString(tag,
                stringValue);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, detailFragment, DetailFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
