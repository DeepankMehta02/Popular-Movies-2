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
import android.widget.TextView;

import com.deepankmehta.popularmovies2.R;
import com.deepankmehta.popularmovies2.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReveiewAdapterViewHolder>{

    private Review[] reviewData;
    private static TextView mReviewTextView;
    private static TextView mAuthorTextView;

    public ReviewAdapter(Review[] review) {
        reviewData = review;
    }
    public class ReveiewAdapterViewHolder extends RecyclerView.ViewHolder {

        public ReveiewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewTextView = (TextView) itemView.findViewById(R.id.review);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.author);
        }
    }

    @Override
    public ReveiewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReveiewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReveiewAdapterViewHolder reveiewAdapterViewHolder, int position) {
        String authorBind = reviewData[position].getAuthor();
        String reviewBind = reviewData[position].getContent();
        mAuthorTextView.setText(authorBind);
        mReviewTextView.setText(reviewBind);
    }

    @Override
    public int getItemCount() {
        if (null == reviewData) {
            return 0;
        }
        return reviewData.length;
    }

}
