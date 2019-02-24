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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deepankmehta.popularmovies2.Movie;
import com.deepankmehta.popularmovies2.R;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovie;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Movie[] movie, MovieAdapterOnClickHandler clickHandler) {
        this.mMovie = movie;
        this.mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMainImage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMainImage = (ImageView) itemView.findViewById(R.id.main_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieBind = mMovie[position].getPoster();
        Picasso.get()
                .load(movieBind)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(movieAdapterViewHolder.mMainImage);
    }

    @Override
    public int getItemCount() {
        if (null == mMovie) {
            return 0;
        }
        return mMovie.length;
    }

}
