package com.freedom_mobile.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freedom_mobile.popularmovies.R;
import com.freedom_mobile.popularmovies.model.Review;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter
        extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private HashMap<String, Review> mReviews;

    public ReviewAdapter(HashMap<String, Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Review review = mReviews.get(String.valueOf(position));
        String authorName = review.getAuthor();
        String reviewContent = review.getContent();

        viewHolder.mAuthorName.setText(authorName);
        viewHolder.mReviewContent.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.author_name) TextView mAuthorName;
        @Bind(R.id.review_content) TextView mReviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
