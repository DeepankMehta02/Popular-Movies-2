/*
 * Copyright 2019 Deepank Mehta. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * You may not use this file; except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * Distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * Limitations under the License.
 *
 */

package com.deepankmehta.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deepankmehta.popularmovies2.adapters.MovieAdapter;
import com.deepankmehta.popularmovies2.utils.NetworkUtility;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private Movie[] moviesData;
    private MovieAdapter mAdapter;
    private String query = "popular";
    RecyclerView mRecyclerview;
    TextView mErrorMessage;
    ProgressBar mLoadingIndicator;

    private static final String LIFECYCLE_CALLBACKS = "callbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LIFECYCLE_CALLBACKS);
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerview = (RecyclerView) findViewById(R.id.main_recyclerview);
        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int numberOfColumns = calculateNoOfColumns(getApplicationContext());
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);

        setupSharedPreferences();
        loadMovies(query);
    }

    private void loadMovieData(String query) {
        showJsonMovieData();
        new FetchMovies().execute(query);
    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSortBy(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void loadSortBy(SharedPreferences sharedPreferences) {
        query = sharedPreferences.getString(this.getResources().getString(R.string.pref_sort_key), this.getResources().getString(R.string.pref_sort_by_popular_key));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getResources().getString(R.string.pref_sort_key))) {
            loadSortBy(sharedPreferences);
            loadMovieData(query);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void loadMovies(String query) {
        showJsonMovieData();
        new FetchMovies().execute(query);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public void showJsonMovieData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerview.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerview.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int position) {
        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(Intent.EXTRA_TEXT, position);
        detailsIntent.putExtra("poster", moviesData[position].getPoster());
        detailsIntent.putExtra("title", moviesData[position].getTitle());
        detailsIntent.putExtra("release", moviesData[position].getDate());
        detailsIntent.putExtra("rate", moviesData[position].getVote());
        detailsIntent.putExtra("overview", moviesData[position].getOverview());
        detailsIntent.putExtra("id", moviesData[position].getId());
        startActivity(detailsIntent);
    }


    public class FetchMovies extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String sortBy = params[0];
            URL movieUrl = NetworkUtility.buildUrl(sortBy);

            try {
                String response = NetworkUtility.getResponseFromHttpUrl(movieUrl);
                moviesData = NetworkUtility.getMovieJson(MainActivity.this, response);
                return moviesData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showJsonMovieData();
                mAdapter = new MovieAdapter(movies, MainActivity.this);
                mRecyclerview.setAdapter(mAdapter);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_favorites) {
            Intent favoritesIntent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(favoritesIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        query = savedInstanceState.getString(LIFECYCLE_CALLBACKS);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String sortBy = query;
        outState.putString(LIFECYCLE_CALLBACKS, sortBy);
        super.onSaveInstanceState(outState);
    }
}
