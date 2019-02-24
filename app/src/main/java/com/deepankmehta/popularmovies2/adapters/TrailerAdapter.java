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
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepankmehta.popularmovies2.R;
import com.deepankmehta.popularmovies2.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailer;
    final String MOVIE_TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";
    private Context context;
    private TextView mTrailerListItem;

    public TrailerAdapter(Trailer[] trailers, Context context) {
        mTrailer = trailers;
        this.context = context;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerListItem = (TextView) itemView.findViewById(R.id.trailer);
        }

    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailerBind = mTrailer[position].getName();
        final String trailerKey = mTrailer[position].getKey();
        mTrailerListItem.setText(trailerBind);
        trailerAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri trailerVideo = Uri.parse(MOVIE_TRAILER_BASE_URL + trailerKey);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerVideo);
                context.startActivity(trailerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mTrailer) {
            return 0;
        }
        return mTrailer.length;
    }

}
