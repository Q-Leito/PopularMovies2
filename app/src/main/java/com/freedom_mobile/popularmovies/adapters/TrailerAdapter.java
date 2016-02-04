package com.freedom_mobile.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freedom_mobile.popularmovies.model.Trailer;
import com.freedom_mobile.popularmovies.utils.Urls;
import com.freedom_mobile.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrailerAdapter
        extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private HashMap<String, Trailer> mTrailers;

    public TrailerAdapter(Context context, HashMap<String, Trailer> trailers) {
        mContext = context;
        mTrailers = trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list, parent, false);

        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Trailer trailer = mTrailers.get(String.valueOf(position));
        final String trailerKey = trailer.getKey();
        String trailerName = trailer.getName();
        String trailerSite = trailer.getSite();
        int  trailerSize = trailer.getSize();
        String  trailerType = trailer.getType();

        Picasso.with(mContext)
                .load(Urls.YOUTUBE_THUMBNAIL
                        + trailerKey + Urls.YOUTUBE_THUMBNAIL_QUALITY)
                .into(viewHolder.mTrailerThumbnail);

        viewHolder.mTrailerName.setText(String.format("%s - %s", trailerName, trailerType));
        viewHolder.mTrailerSite.setText(String.format("Site - %s", trailerSite));
        viewHolder.mTrailerSize.setText(String.format("Quality - %sp", trailerSize));
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailer_thumbnail) ImageView mTrailerThumbnail;
        @Bind(R.id.trailer_name) TextView mTrailerName;
        @Bind(R.id.trailer_site) TextView mTrailerSite;
        @Bind(R.id.trailer_size) TextView mTrailerSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
