package com.solutionco.android.movies.utilities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.solutionco.android.movies.Data.DbHelper;
import com.solutionco.android.movies.Data.Movie;
import com.solutionco.android.movies.Data.MovieDBContract;

/**
 * Created by Ahmed on 4/10/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    DbHelper openHelper;
    private static final String DBName  ="movies.db";
    public static final int MOVIES_ALL = 10;
    public static final int ONE_MOVIE = 1;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDBContract.AUTHORITY,MovieDBContract.MOVIE_PATH,MOVIES_ALL);
        uriMatcher.addURI(MovieDBContract.AUTHORITY,MovieDBContract.MOVIE_PATH +"/*",ONE_MOVIE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db  = openHelper.getReadableDatabase();

        Cursor returnCursor;
        int code = sUriMatcher.match(uri);
        switch (code){
            case MOVIES_ALL:
                returnCursor = db.query(MovieDBContract.MovieEntry.TABLE_NAME ,
                        null ,
                        null,
                        null,
                        null,
                        null,
                        null);

                break;
            case ONE_MOVIE:
                String id = uri.getPathSegments().get(1);

                String mSelection = MovieDBContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
                String [] mSelectionArgs = new String[]{id};
                returnCursor = db.query(MovieDBContract.MovieEntry.TABLE_NAME ,
                        null,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("UnknownURI" + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db  = openHelper.getWritableDatabase();

        int code = sUriMatcher.match(uri);

        Uri returnUri;
        switch (code){
            case MOVIES_ALL:
                long id = db.insert(MovieDBContract.MovieEntry.TABLE_NAME , null , values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(MovieDBContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("UnknownURI" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db  = openHelper.getReadableDatabase();
        int ret;
        int code = sUriMatcher.match(uri);
        switch (code){
            case ONE_MOVIE:
                String id = uri.getPathSegments().get(1);

                String mSelection = MovieDBContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
                String [] mSelectionArgs = new String[]{id};
                ret = db.delete(MovieDBContract.MovieEntry.TABLE_NAME ,mSelection,mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("UnknownURI" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ret;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
