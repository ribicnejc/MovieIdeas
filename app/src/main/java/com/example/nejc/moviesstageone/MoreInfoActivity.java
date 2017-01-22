package com.example.nejc.moviesstageone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoreInfoActivity extends AppCompatActivity {

    public TextView mTitleTextView;
    public TextView mAvgVoteTextView;
    public TextView mReleaseDateTextView;
    public TextView mOverviewTextView;
    public ImageView mPosterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title_more_info);
        mAvgVoteTextView = (TextView) findViewById(R.id.tv_avg_vote_more_info);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_date_more_info);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview_more_info);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_background_more_info);


        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_TITLE)){
            mTitleTextView.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));
        }
        if(intent.hasExtra(MainActivity.EXTRA_RELEASE_DATE)){
            mReleaseDateTextView.setText(
                    getResources().getString(R.string.release_date)
                    + " "
                    + intent.getStringExtra(MainActivity.EXTRA_RELEASE_DATE));
        }
        if(intent.hasExtra(MainActivity.EXTRA_AVG_VOTE)){
            mAvgVoteTextView.setText(
                    getResources().getString(R.string.average_vote)
                    + " "
                    + intent.getStringExtra(MainActivity.EXTRA_AVG_VOTE));
        }
        if(intent.hasExtra(MainActivity.EXTRA_OVERVIEW)){
            mOverviewTextView.setText(
                    getResources().getString(R.string.overview)
                    + "\n\n"
                    + intent.getStringExtra(MainActivity.EXTRA_OVERVIEW));
        }
        if(intent.hasExtra(MainActivity.EXTRA_POSTER_PATH)){
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w342/" + intent.getStringExtra(MainActivity.EXTRA_POSTER_PATH))
                    .into(mPosterImageView);
        }
    }
}
