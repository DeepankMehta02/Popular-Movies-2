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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deepankmehta.popularmovies2.adapters.ReviewAdapter;
import com.deepankmehta.popularmovies2.adapters.TrailerAdapter;
import com.deepankmehta.popularmovies2.data.FavoriteContract;
import com.deepankmehta.popularmovies2.utils.NetworkUtility;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class FavoriteDetailsActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDate;
    private TextView mVote;
    private TextView mOverview;

    private String poster = "";
    private String title = "";
    private String date = "";
    private String vote = "";
    private String overview = "";
    private int id = 0;

    private RecyclerView mRecyclerViewTrailer;
    private Trailer[] trailerData;
    private TrailerAdapter mTrailerAdapter;
    private TextView mTrailerErrorMessage;

    private RecyclerView mRecyclerViewReview;
    private Review[] reviewData;
    private ReviewAdapter mReviewAdapter;
    private TextView mReviewErrorMessage;

    String[] mProjection = {FavoriteContract.FavoriteAdd._ID,
            FavoriteContract.FavoriteAdd.MOVIE_ID};
    private String[] mSelectionArgs = {""};
    private String mSelectionClause;

    private SQLiteDatabase mDb;
    private Button mButton;
    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTrailerErrorMessage = (TextView) findViewById(R.id.trailer_error_message);
        mRecyclerViewTrailer = (RecyclerView) findViewById(R.id.trailer_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewTrailer.setLayoutManager(linearLayoutManager);
        mRecyclerViewTrailer.setHasFixedSize(true);
        mRecyclerViewTrailer.setAdapter(mTrailerAdapter);

        mReviewErrorMessage = (TextView) findViewById(R.id.review_error_message);
        mRecyclerViewReview = (RecyclerView) findViewById(R.id.review_recyclerview);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        mRecyclerViewReview.setLayoutManager(linearLayout);
        mRecyclerViewReview.setHasFixedSize(true);
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mDate = (TextView) findViewById(R.id.date);
        mVote = (TextView) findViewById(R.id.vote);
        mOverview = (TextView) findViewById(R.id.overview);

        mButton = (Button) findViewById(R.id.button);

        poster = getIntent().getStringExtra("poster");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("release");
        vote = getIntent().getStringExtra("rate");
        overview = getIntent().getStringExtra("overview");
        id = getIntent().getIntExtra("id", 0);

        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(mPoster);

        mTitle.setText(title);
        mDate.setText(date);
        mVote.setText(vote);
        mOverview.setText(overview);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if (isMovieFavorited(id)) {
                if (isMovieFavorited(String.valueOf(id))) {
                    removeFavorites(String.valueOf(id));

                    Context context = getApplicationContext();
                    CharSequence removedFavorites = "This movie is removed from your favorites.";
                    Toast toast = Toast.makeText(context, removedFavorites, Toast.LENGTH_SHORT);
                    toast.show();

                    mButton.setText("Add");
                } else {
                    addFavorites(title, id, poster, date, vote, overview);
                    Context context = getApplicationContext();
                    CharSequence addedFavorites = "This movie is added to your favorites.";
                    Toast toast = Toast.makeText(context, addedFavorites, Toast.LENGTH_SHORT);
                    toast.show();

                    mButton.setText("Remove");
                }
            }
        });

        loadTrailer();
        loadReview();
        isMovieFavorited(String.valueOf(id));
    }

    private void loadTrailer() {
        String trailerId = String.valueOf(id);
        new FetchTrailer().execute(trailerId);
    }

    private void loadReview() {
        String reviewId = String.valueOf(id);
        new FetchReview().execute(reviewId);
    }

    public class FetchTrailer extends AsyncTask<String, Void, Trailer[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            URL trailerUrl = NetworkUtility.buildTrailerUrl(id);

            try {
                String response = NetworkUtility.getResponseFromHttpUrl(trailerUrl);
                trailerData = NetworkUtility.getTrailerJson(FavoriteDetailsActivity.this, response);
                return trailerData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers != null) {
                mTrailerAdapter = new TrailerAdapter(trailers, FavoriteDetailsActivity.this);
                mRecyclerViewTrailer.setAdapter(mTrailerAdapter);
            } else {
                mTrailerErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FetchReview extends AsyncTask<String, Void, Review[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            URL reviewUrl = NetworkUtility.buildReviewUrl(id);

            try {
                String response = NetworkUtility.getResponseFromHttpUrl(reviewUrl);
                reviewData = NetworkUtility.getReviewJson(FavoriteDetailsActivity.this, response);
                return reviewData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews != null) {
                mReviewAdapter = new ReviewAdapter(reviews);
                mRecyclerViewReview.setAdapter(mReviewAdapter);
            } else {
                mReviewErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addFavorites(String name, int id, String poster, String date, String vote, String overview) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_ID, id);
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_NAME, name);
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_POSTER, poster);
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_RELEASE, date);
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_RATE, vote);
        contentValues.put(FavoriteContract.FavoriteAdd.MOVIE_OVERVIEW, overview);
        mUri = getContentResolver().insert(FavoriteContract.FavoriteAdd.CONTENT_URI,
                contentValues);
    }

    private void removeFavorites(String id) {
        mSelectionClause = FavoriteContract.FavoriteAdd.MOVIE_ID + " LIKE ?";
        String[] selectionArgs = new String[]{id};
        getContentResolver().delete(
                FavoriteContract.FavoriteAdd.CONTENT_URI,
                mSelectionClause,
                selectionArgs
        );
    }
    //check if the id exist in database
    public boolean isMovieFavorited(String id){
        mSelectionClause = FavoriteContract.FavoriteAdd.MOVIE_ID + " = ?";
        mSelectionArgs[0] = id;
        Cursor mCursor = getContentResolver().query(
                FavoriteContract.FavoriteAdd.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);

        if(mCursor.getCount() == 0){
            mCursor.close();
            mButton.setText("Add");
            return false;
        }
        mCursor.close();
        mButton.setText("Remove");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
