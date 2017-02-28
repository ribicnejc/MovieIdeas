package com.example.nejc.moviesstageone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejc.moviesstageone.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoreInfoActivity extends AppCompatActivity {
    //TODO add content description to images
    public TextView mTitleTextView;
    public TextView mAvgVoteTextView;
    public TextView mReleaseDateTextView;
    public TextView mOverviewTextView;
    public ImageView mPosterImageView;
    public ImageView mFavoriteImageView;

    public static final String TAG = MoreInfoActivity.class.getSimpleName();

    public String moviePosterPath;
    public int movieId;
    public String movieTitle;
    public float movieAvgRate;
    public String movieOverview;
    public String movieBackgroundPath;
    public String movieReleaseDate;
    public static boolean mMarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title_more_info);
        mAvgVoteTextView = (TextView) findViewById(R.id.tv_avg_vote_more_info);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_date_more_info);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview_more_info);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_background_more_info);
        mFavoriteImageView = (ImageView) findViewById(R.id.iv_favorite_star);


        Intent intent = getIntent();
        setExtras(intent);
        setStarResources(movieId);
    }

    public void favoriteMovie(View view){
        if (!mMarked) markAsFavorite();
        else unMarkAsFavorite();
    }

    public void markAsFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieTitle);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, moviePosterPath);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieAvgRate);

        List<ContentValues> values = new ArrayList<>();
        values.add(contentValues);
        this.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,
                values.toArray(new ContentValues[1]));
        Toast.makeText(this, "Marked as favorite", Toast.LENGTH_SHORT).show();
        setStarImage(true);
    }

    public void unMarkAsFavorite(){
        String stringId = movieId + "";
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        getContentResolver().delete(uri, null, null);

        Toast.makeText(this, "Unmarked as favorite", Toast.LENGTH_SHORT).show();
        setStarImage(false);
    }


    public void setStarResources(final int movieID){

        AsyncTask<Cursor, Cursor, Cursor> mAsyncTask;
        mAsyncTask = new AsyncTask<Cursor, Cursor, Cursor>() {
            @Override
            protected Cursor doInBackground(Cursor... cursors) {
                try{
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                }catch (Exception e){
                    Log.v(TAG, "Failed to query data");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                if (cursor == null) return;
                boolean isMarked = false;
                if (cursor.moveToFirst()){
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                        if (id == movieID){
                            isMarked = true;
                        }
                    }while (cursor.moveToNext());
                }
                setStarImage(isMarked);
                super.onPostExecute(cursor);
            }
        };
        mAsyncTask.execute();
    }

    public void setStarImage(boolean isMarked){
        mMarked = isMarked;
        if (isMarked){
            mFavoriteImageView.setImageResource(R.drawable.favorite_marked);
        }else
            mFavoriteImageView.setImageResource(R.drawable.favorite_unmarked);
    }


    public void setExtras(Intent intent){
        if (intent.hasExtra(MainActivity.EXTRA_TITLE)) {
            movieTitle = intent.getStringExtra(MainActivity.EXTRA_TITLE);
            mTitleTextView.setText(movieTitle);
        }
        if (intent.hasExtra(MainActivity.EXTRA_RELEASE_DATE)) {
            movieReleaseDate = intent.getStringExtra(MainActivity.EXTRA_RELEASE_DATE);
            mReleaseDateTextView.setText(
                    getResources().getString(R.string.release_date)
                            + " "
                            + movieReleaseDate);
        }
        if (intent.hasExtra(MainActivity.EXTRA_AVG_VOTE)) {
            movieAvgRate = Float.parseFloat(intent.getStringExtra(MainActivity.EXTRA_AVG_VOTE));
            mAvgVoteTextView.setText(
                    getResources().getString(R.string.average_vote)
                            + " "
                            + String.valueOf(movieAvgRate));
        }
        if (intent.hasExtra(MainActivity.EXTRA_OVERVIEW)) {
            movieOverview = intent.getStringExtra(MainActivity.EXTRA_OVERVIEW);
            mOverviewTextView.setText(
                    getResources().getString(R.string.overview)
                            + "\n\n"
                            + movieOverview);
        }
        if (intent.hasExtra(MainActivity.EXTRA_POSTER_PATH)) {
            movieBackgroundPath = intent.getStringExtra(MainActivity.EXTRA_POSTER_PATH);
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w342/" + movieBackgroundPath)
                    .into(mPosterImageView);
        }
        if (intent.hasExtra(MainActivity.EXTRA_MOVIE_ID)) {
            movieId = intent.getIntExtra(MainActivity.EXTRA_MOVIE_ID, 0);
        }
        if (intent.hasExtra(MainActivity.EXTRA_MAIN_POSTER_PATH)) {
            moviePosterPath = intent.getStringExtra(MainActivity.EXTRA_MAIN_POSTER_PATH);
        }
    }

}
