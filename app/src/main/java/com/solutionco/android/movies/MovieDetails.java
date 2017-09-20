package com.solutionco.android.movies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.solutionco.android.movies.Data.Movie;
import com.solutionco.android.movies.Data.MovieDBContract;
import com.solutionco.android.movies.Data.Review;
import com.solutionco.android.movies.Data.Trailer;
import com.solutionco.android.movies.utilities.MoviesContentProvider;
import com.solutionco.android.movies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.solutionco.android.movies.MainActivity.EXTRA_BACKDROPPATH;
import static com.solutionco.android.movies.MainActivity.EXTRA_MOVIEID;
import static com.solutionco.android.movies.MainActivity.EXTRA_Overview;
import static com.solutionco.android.movies.MainActivity.EXTRA_PosterPath;
import static com.solutionco.android.movies.MainActivity.EXTRA_ReleaseDate;
import static com.solutionco.android.movies.MainActivity.EXTRA_SORTMETHOD;
import static com.solutionco.android.movies.MainActivity.EXTRA_Title;
import static com.solutionco.android.movies.MainActivity.EXTRA_UserRate;

public class MovieDetails extends AppCompatActivity implements TrailersAdapter.listItemClickListener {

    ImageView poster;
    ImageView backgroundPoster;
    TextView title;
    TextView releaseDate;
    TextView userRating;
    TextView overview;
    Toolbar myToolbar;
    FloatingActionButton addToFavourites;

    Movie currentMovie;



    TrailersAdapter trailersAdapter;
    RecyclerView trailersRecyclerView;
    LinearLayoutManager trailerslayoutManager;

    ReviewsAdapter reviewsAdapter;
    RecyclerView reviewsRecyclerView;
    LinearLayoutManager reviewslayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        currentMovie = new Movie();
        poster = new ImageView(this);
        backgroundPoster = (ImageView) findViewById(R.id.back_drop);
        title = (TextView)findViewById(R.id.movie_title0);
        releaseDate = (TextView)findViewById(R.id.release_date);
        userRating = (TextView)findViewById(R.id.user_rate);
        overview = (TextView)findViewById(R.id.overview);
        addToFavourites = (FloatingActionButton) findViewById(R.id.add_to_favourites);
        Intent intent = getIntent();

        currentMovie.setMovie_ID(intent.getStringExtra(EXTRA_MOVIEID));


        currentMovie.setPosterPath(intent.getStringExtra(EXTRA_PosterPath));
        currentMovie.setOriginalTitle(intent.getStringExtra(EXTRA_Title));
        currentMovie.setReleaseDate(intent.getStringExtra(EXTRA_ReleaseDate).substring(0,4));
        currentMovie.setVoteAverage(intent.getStringExtra(EXTRA_UserRate));
        currentMovie.setOverview(intent.getStringExtra(EXTRA_Overview));
        currentMovie.setBackdrop_path(intent.getStringExtra(EXTRA_BACKDROPPATH));

        title.setText(currentMovie.getOriginalTitle());
        releaseDate.setText(currentMovie.getReleaseDate());
        userRating.setText(currentMovie.getVoteAverage());
        overview.setText(currentMovie.getOverview());


        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
        trailersRecyclerView.setNestedScrollingEnabled(false);

        reviewsRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        reviewsRecyclerView.setNestedScrollingEnabled(false);

        trailerslayoutManager = new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL ,false);
        reviewslayoutManager = new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL ,false);



        if(intent.getStringExtra(EXTRA_SORTMETHOD).equals("favourites")){
            new CheckDataBase(this , currentMovie.getMovie_ID()).execute();
            currentMovie.setFavourite(true);
            addToFavourites.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(221,44,0)));

        }
        else {
            new AmIFavourite(this , currentMovie.getMovie_ID()).execute();
            Picasso.with(this)
                    .load(currentMovie.getPosterPath())
                    .into(poster);
            Picasso.with(this)
                    .load(currentMovie.getBackdrop_path())
                    .into(backgroundPoster);
        }

        if(isConnectedToNetwork()){
            new DetailsLoader(this).execute();
        }


    }

    public void addMovieToFavourites (View V){
        if(currentMovie.isFavourite()){
            Uri uri = MovieDBContract.MovieEntry.CONTENT_URI.buildUpon()
                    .appendEncodedPath(currentMovie.getMovie_ID()).build();
            int x = getContentResolver().delete(uri,null,null);
            if( x == 1)
            {
                addToFavourites.setBackgroundColor(Color.rgb(96,125,139));
                addToFavourites.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(96,125,139)));
                currentMovie.setFavourite(false);
                Toast.makeText(this , "Removed successfully from favourites" ,Toast.LENGTH_SHORT).show();
            }
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_ID , currentMovie.getMovie_ID());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_RATE , currentMovie.getVoteAverage());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE , currentMovie.getReleaseDate());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_OVERVIEW , currentMovie.getOverview());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_TITLE , currentMovie.getOriginalTitle());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

        Bitmap bitmap = ((BitmapDrawable)poster.getDrawable()).getBitmap();
        Bitmap bitmap2 = ((BitmapDrawable)backgroundPoster.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, bos2);


        byte[] posterImage = bos.toByteArray();
        byte[] posterImage2 = bos2.toByteArray();

        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER , posterImage);
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER_BACK , posterImage2);


        Uri uri = getContentResolver().insert(MovieDBContract.MovieEntry.CONTENT_URI , contentValues);

        if(uri!= null)
        {
            currentMovie.setFavourite(true);
            addToFavourites.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DD2C00")));
            Toast.makeText(this , "Added successfully to favourites" ,Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap convertCursorToBitmap(Cursor cursor , int x){
        int pos = 0;
        cursor.moveToPosition(pos);
        byte[] byteArray;
        //0 for poster
        //1 for back_poster
        if (x==0){
            byteArray =cursor.getBlob(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER));
        }else{
            byteArray =cursor.getBlob(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER_BACK));
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
        return bitmap;
    }
    public static boolean convertCursorToBoolean(Cursor cursor){
        int pos = 0;
        return (cursor.moveToPosition(pos));
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String url ="https://www.youtube.com/watch?" + currentMovie.getTrailers().get(clickedItemIndex).getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
        startActivity(intent);
    }

    public class DetailsLoader extends AsyncTask{
        Context context;

        public DetailsLoader(Context co){
            context = co;
        }
        @Override
        protected Object doInBackground(Object[] params) {

            try {
                currentMovie.setTrailers(NetworkUtilities.getTrailers(currentMovie.getMovie_ID()));
                currentMovie.setReviews(NetworkUtilities.getReviews(currentMovie.getMovie_ID()));
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            trailersAdapter = new TrailersAdapter(context , currentMovie.getTrailers() , (TrailersAdapter.listItemClickListener) context);
            trailersAdapter.notifyDataSetChanged();
            trailersRecyclerView.setHasFixedSize(true);
            trailersRecyclerView.setLayoutManager(trailerslayoutManager);
            trailersRecyclerView.setAdapter(trailersAdapter);


            reviewsAdapter = new ReviewsAdapter(context , currentMovie.getReviews());
            reviewsAdapter.notifyDataSetChanged();
            reviewsRecyclerView.setHasFixedSize(true);
            reviewsRecyclerView.setLayoutManager(reviewslayoutManager);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        }
    }


    public class CheckDataBase extends AsyncTask{
        Context context;
        String id;
        public CheckDataBase(Context co , String d){
            context = co;
            this.id = d;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            Uri uri = MovieDBContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(id).build();
            Cursor cursor = getContentResolver().query(uri,
                    null, null , null , null);
            currentMovie.setPoster(convertCursorToBitmap(cursor, 0));
            currentMovie.setBackPoster(convertCursorToBitmap(cursor, 1));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            poster.setImageBitmap(currentMovie.getPoster());
            backgroundPoster.setImageBitmap(currentMovie.getBackPoster());
        }
    }


    public class AmIFavourite extends AsyncTask{
        Context context;
        String id;
        public AmIFavourite(Context co , String d){
            context = co;
            this.id = d;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            Uri uri = MovieDBContract.MovieEntry.CONTENT_URI.buildUpon().appendEncodedPath(id).build();
            Cursor cursor = getContentResolver().query(uri,
                    null, null , null , null);
            currentMovie.setFavourite(convertCursorToBoolean(cursor));

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (currentMovie.isFavourite()){
                addToFavourites.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(221,44,0)));
            }
            else
            {
                addToFavourites.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(96,125,139)));
            }

        }
    }
}
