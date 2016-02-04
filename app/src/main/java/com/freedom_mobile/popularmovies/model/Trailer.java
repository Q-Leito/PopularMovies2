package com.freedom_mobile.popularmovies.model;

import java.util.HashMap;

public class Trailer {

    private String mId;
    private String mTrailerId;
    private String mKey;
    private String mName;
    private String mSite;
    private int mSize;
    private String mType;
    public static HashMap<String, Trailer> mTrailerList = new HashMap<>();

    public Trailer() {
        // Empty constructor.
    }

    public Trailer(String id, String trailerId, String key, String name,
                   String site, int size, String type) {
        mId = id;
        mTrailerId = trailerId;
        mKey = key;
        mName = name;
        mSite = site;
        mSize = size;
        mType = type;
    }

    public String getId() {
        return mId;
    }

    public String getTrailerId() {
        return mTrailerId;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public int getSize() {
        return mSize;
    }

    public String getType() {
        return mType;
    }

    public void addTrailer(Trailer trailerFromTMDB) {
        if (!mTrailerList.containsKey(trailerFromTMDB.getId())) {
            mTrailerList.put(trailerFromTMDB.getId(), trailerFromTMDB);
        }
    }
}
