package com.example.nejc.moviesstageone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nejc.moviesstageone.R;
import com.example.nejc.moviesstageone.objects.Trailer;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private final TrailersAdapter.TrailersAdapterOnClickHandler mClickHandler;

   public interface TrailersAdapterOnClickHandler{
        void trailersOnClick(int clickedItemIndex);
    }


    public TrailersAdapter(ArrayList<Trailer> mTrailers, TrailersAdapterOnClickHandler mClickHandler){
        this.mClickHandler = mClickHandler;
        this.mTrailers = mTrailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.mTrailerTitle.setText(mTrailers.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTrailerTitle;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerTitle = (TextView) itemView.findViewById(R.id.tv_trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.trailersOnClick(clickedPosition);
        }
    }
}
