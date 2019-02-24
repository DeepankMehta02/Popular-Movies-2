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

package com.deepankmehta.popularmovies2.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deepankmehta.popularmovies2.FavoriteActivity;
import com.deepankmehta.popularmovies2.FavoriteDetailsActivity;
import com.deepankmehta.popularmovies2.R;
import com.deepankmehta.popularmovies2.data.FavoriteContract;
import com.squareup.picasso.Picasso;

public class FavoriteCursorAdapter extends RecyclerView.Adapter<FavoriteCursorAdapter.FavoriteViewHolder>{

    private Cursor mCursor;
    private Context mContext;
    private String name;
    public String poster;
    public int id;

    public FavoriteCursorAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder favoriteViewHolder, int position) {
        int idIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteAdd._ID);
        int posterIndex = mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_POSTER);
        mCursor.moveToPosition(position);
        id = mCursor.getInt(idIndex);
        poster = mCursor.getString(posterIndex);
        favoriteViewHolder.itemView.setTag(id);
        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(favoriteViewHolder.mFavoriteImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mFavoriteImageView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            mFavoriteImageView = (ImageView) itemView.findViewById(R.id.main_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_NAME));
            int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_ID));
            String poster = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_POSTER));
            String date = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_RELEASE));
            String vote = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_RATE));
            String overview = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteAdd.MOVIE_OVERVIEW));
            Intent favoriteDetailsIntent = new Intent(mContext, FavoriteDetailsActivity.class);
            favoriteDetailsIntent.putExtra(Intent.EXTRA_TEXT, FavoriteDetailsActivity.class);
            favoriteDetailsIntent.putExtra("title", name);
            favoriteDetailsIntent.putExtra("id", id);
            favoriteDetailsIntent.putExtra("poster", poster);
            favoriteDetailsIntent.putExtra("release", date);
            favoriteDetailsIntent.putExtra("average", vote);
            favoriteDetailsIntent.putExtra("overview", overview);
            mContext.startActivity(favoriteDetailsIntent);
        }
    }
}
