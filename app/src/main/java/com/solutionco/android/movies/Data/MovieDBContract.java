package com.solutionco.android.movies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ahmed on 4/10/2017.
 */

public class MovieDBContract {

    public static final String AUTHORITY = "com.solutionco.android.movies";

    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);

    public static final String MOVIE_PATH = "movie";


    private MovieDBContract(){

    }
    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(MOVIE_PATH).build();


        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASEDATE = "release_date";
        public static final String COLUMN_MOVIE_RATE = "rate";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_POSTER_BACK = "poster_back";
    }
}
