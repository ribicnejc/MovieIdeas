package com.example.nejc.moviesstageone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nejc.moviesstageone.R;
import com.example.nejc.moviesstageone.objects.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private ArrayList<Review> mReviews;
    private final ReviewsAdapter.ReviewsAdapterOnClickHandler mClickHandler;


    public interface ReviewsAdapterOnClickHandler {
        void reviewsOnClick(int clickedItemIndex);
    }

    public ReviewsAdapter(ArrayList<Review> mReviews, ReviewsAdapterOnClickHandler mClickHandler){
        this.mReviews = mReviews;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reviews_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.mAuthor.setText(mReviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mAuthor;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.reviewsOnClick(clickedPosition);
        }
    }
}
