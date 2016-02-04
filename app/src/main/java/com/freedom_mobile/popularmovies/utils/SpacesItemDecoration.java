package com.freedom_mobile.popularmovies.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public SpacesItemDecoration(int itemOffSet) {
        mItemOffset = itemOffSet;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
