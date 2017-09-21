package com.solutionco.android.movies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 4/10/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String stmt = "CREATE TABLE " + MovieDBContract.MovieEntry.TABLE_NAME + "( " + MovieDBContract.MovieEntry.COLUMN_MOVIE_ID
                + " TEXT NOT NULL PRIMARY KEY " + "," + MovieDBContract.MovieEntry.COLUMN_MOVIE_TITLE
                + " TEXT NOT NULL , " + MovieDBContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE +
                " TEXT NOT NULL , " + MovieDBContract.MovieEntry.COLUMN_MOVIE_RATE +
                " TEXT NOT NULL , " + MovieDBContract.MovieEntry.COLUMN_MOVIE_OVERVIEW +
                " TEXT NOT NULL , " + MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER +
                " BLOB , " + MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER_BACK + " BLOB "
                + " );";
        db.execSQL(stmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDBContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
