package com.freedom_mobile.popularmovies.model;

import java.util.HashMap;

public class Review {

    private String mId;
    private String mReviewId;
    private String mAuthor;
    private String mContent;
    public static HashMap<String, Review> mReviewList = new HashMap<>();

    public Review() {
        // Empty constructor.
    }

    public Review(String id, String reviewId, String author,
                  String content) {
        mId = id;
        mReviewId = reviewId;
        mAuthor = author;
        mContent = content;
    }

    public String getId() {
        return mId;
    }

    public String getReviewId() {
        return mReviewId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public void addReview(Review reviewFromTMDB) {
        if (!mReviewList.containsKey(reviewFromTMDB.getId())) {
            mReviewList.put(reviewFromTMDB.getId(), reviewFromTMDB);
        }
    }
}
