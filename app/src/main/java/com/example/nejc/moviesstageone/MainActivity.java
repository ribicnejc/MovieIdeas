package com.example.nejc.moviesstageone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejc.moviesstageone.adapters.MoviesAdapter;
import com.example.nejc.moviesstageone.networkutils.NetworkUtils;
import com.example.nejc.moviesstageone.objects.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_OVERVIEW = "extra_overview";
    public static final String EXTRA_POSTER_PATH = "poster_path";
    public static final String EXTRA_RELEASE_DATE = "release_date";
    public static final String EXTRA_AVG_VOTE = "average_vote";
    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_MAIN_POSTER_PATH = "main_poster_path";

    RecyclerView mRecyclerView;
    MoviesAdapter mAdapter;
    ProgressBar mProgressBar;
    TextView mErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_rv);
        mErrorMsg = (TextView) findViewById(R.id.tv_error);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        new FetchMovies("popular").execute();
        mRecyclerView.setHasFixedSize(true);


    }

    class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
        String type;

        FetchMovies(String type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMsg.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            String jsonResponse;
            ArrayList<Movie> movieArrayList = new ArrayList<>();
            URL mUrl = NetworkUtils.buildUrl(type, getBaseContext().getResources().getString(R.string.api_key));
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(mUrl);
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String posterPath = jsonObject1.getString("poster_path");
                    int id = jsonObject1.getInt("id");
                    boolean adult = jsonObject1.getBoolean("adult");
                    String overview = jsonObject1.getString("overview");
                    String releaseDate = jsonObject1.getString("release_date");
                    String originalTitle = jsonObject1.getString("original_title");
                    String originalLanguage = jsonObject1.getString("original_language");
                    String title = jsonObject1.getString("title");
                    String backdropPath = jsonObject1.getString("backdrop_path");
                    int voteCount = jsonObject1.getInt("vote_count");
                    boolean video = jsonObject1.getBoolean("video");
                    double voteAvg = jsonObject1.getDouble("vote_average");

                    Movie movie = new Movie();
                    movie.setPosterPath(posterPath);
                    movie.setId(id);
                    movie.setAdult(adult);
                    movie.setOverview(overview);
                    movie.setReleaseDate(releaseDate);
                    movie.setOriginalTitle(originalTitle);
                    movie.setOriginalLanguage(originalLanguage);
                    movie.setTitle(title);
                    movie.setBackdropPath(backdropPath);
                    movie.setVoteCount(voteCount);
                    movie.setVideo(video);
                    movie.setVoteAverage(voteAvg);

                    movieArrayList.add(movie);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return movieArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies == null) {
                mErrorMsg.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                return;
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mErrorMsg.setVisibility(View.INVISIBLE);
            mAdapter = new MoviesAdapter(movies, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            Toast.makeText(MainActivity.this, "Sorted by " + type, Toast.LENGTH_SHORT).show();
            super.onPostExecute(movies);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            mAdapter = null;
            new FetchMovies("popular").execute();
            return true;
        }
        if (id == R.id.action_highest_rated) {
            mAdapter = null;
            new FetchMovies("top_rated").execute();
            return true;
        }
        if (id == R.id.action_favorites) {
            //TODO show favorite movies
            /**
             * first set adapter to null then start loading and start cursor loader,
             * inside fetch movies from db with content provider, fill adapter with data,
             * and show data on post execute
             */
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MoreInfoActivity.class);
        Movie movie = mAdapter.getMovie(clickedItemIndex);
        intent.putExtra(EXTRA_TITLE, movie.getTitle());
        intent.putExtra(EXTRA_RELEASE_DATE, movie.getReleaseDate());
        intent.putExtra(EXTRA_POSTER_PATH, movie.getBackdropPath());
        intent.putExtra(EXTRA_AVG_VOTE, movie.getVoteAverage() + "");
        intent.putExtra(EXTRA_OVERVIEW, movie.getOverview());
        intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
        intent.putExtra(EXTRA_MAIN_POSTER_PATH, movie.getPosterPath());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(getPackageName(), "Couldn't start activity MoreInfo");
        }
    }
    //TODO on save instance when display rotate you have to stay on same point when scrolling
}
