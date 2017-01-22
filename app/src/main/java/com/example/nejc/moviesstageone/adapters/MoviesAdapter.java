package com.example.nejc.moviesstageone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nejc.moviesstageone.R;
import com.example.nejc.moviesstageone.objects.Movie;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMovies;
    private final MoviesAdapterOnClickHandler mClickHandler;


    public interface MoviesAdapterOnClickHandler {
        void onClick(int clickedItemIndex);
    }

    public MoviesAdapter(ArrayList<Movie> mMovies, MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
        this.mMovies = mMovies;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w342/" + mMovies.get(position).getPosterPath())
                .into(holder.movieThumbnail);
        //w185
        holder.movieTitle.setText(mMovies.get(position).getTitle());
    }

    public Movie getMovie(int i){
        return mMovies.get(i);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieTitle;
        ImageView movieThumbnail;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);

        }
    }
}
