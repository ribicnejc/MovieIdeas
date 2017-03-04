package com.example.nejc.moviesstageone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 4;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID                            + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID                + " INTEGER NOT NULL, "                     +
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE          + " VARCHAR(255) NOT NULL, "                +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH             + " VARCHAR(255) NOT NULL, "                +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE            + " VARCHAR(255) NOT NULL, "                +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE            + " REAL NOT NULL, "                        +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW          + " LONG TEXT, "                            +
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKGROUND_PATH   + " LONG TEXT, "                            +
                        "UNIQUE (" + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
