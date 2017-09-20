package com.solutionco.android.movies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.solutionco.android.movies.Data.Movie;
import com.solutionco.android.movies.Data.MovieDBContract;
import com.solutionco.android.movies.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>
        ,PopupMenu.OnMenuItemClickListener  , MovieAdapter.ListItemClickListener{

    RecyclerView recyclerView;
    MovieAdapter adapter;
    GridLayoutManager layoutManager;
    ArrayList<Movie> movies = null;
    Toolbar myToolbar;
    String sort;
    TextView problemDescription;
    public static final String BUNDLE_KEY= "com.solutionco.android.movies.OnaSaveInstanceState_BundleKey";

    public static final String EXTRA_MOVIEID= "com.solutionco.android.movies.MovieID";
    public static final String EXTRA_Title= "com.solutionco.android.movies.Title";
    public static final String EXTRA_Overview= "com.solutionco.android.movies.Overview";
    public static final String EXTRA_UserRate= "com.solutionco.android.movies.UserRate";
    public static final String EXTRA_ReleaseDate= "com.solutionco.android.movies.ReleaseDate";
    public static final String EXTRA_PosterPath= "com.solutionco.android.movies.PosterPath";
    public static final String EXTRA_SORTMETHOD= "com.solutionco.android.movies.sortMethod";
    public static final String EXTRA_BACKDROPPATH= "com.solutionco.android.movies.backdropPath";

    LoaderManager loaderManager = getSupportLoaderManager();
    ProgressDialog progress;
    private static final int LOADER_ID = 1026;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState!=null){
           sort = savedInstanceState.getString(BUNDLE_KEY);
        }
        else {
            sort="popular";
        }

        progress = new ProgressDialog(this);
        progress.setTitle("");
        progress.setMessage("Getting Movies...");
        progress.setCancelable(false);
        progress.show();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        movies = new ArrayList<>();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY , sort);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loaderManager.destroyLoader(LOADER_ID);


        if(isConnectedToNetwork()){
            loaderManager.initLoader(LOADER_ID, null , this);
        }else{
            progress.dismiss();
            setContentView(R.layout.activity_main_connection_problem);
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            problemDescription = (TextView)findViewById(R.id.connectivity_problem_text_view);
            problemDescription.setText(R.string.no_network);
            switch(sort){
                case "popular":
                    myToolbar.setTitle("Popular Movies");
                    break;
                case "topRated":
                    myToolbar.setTitle("Top Rated Movies");
                    break;
                case "favourites":
                    myToolbar.setTitle("Favourites");
                    break;
            }
        }

        //if (sort.equals("favourites")){
          //  loaderManager.destroyLoader(LOADER_ID);
            //loaderManager.initLoader(LOADER_ID, null , this);
            //new Fetch(this , "favourites" ).execute();
        //}
    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showPopup(myToolbar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.pop_up_menu, popup.getMenu());
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular:
                if(!sort.equals("popular")){
                    progress = new ProgressDialog(this);
                    progress.setTitle("");
                    progress.setMessage("Getting Movies...");
                    progress.setCancelable(false);
                    progress.show();

                    sort="popular";
                    loaderManager.destroyLoader(LOADER_ID);
                    loaderManager.initLoader(LOADER_ID, null , this);
                    myToolbar.setTitle("Popular Movies");
                    //new Fetch(this , sort).execute();
                }
                return true;
            case R.id.top_rated:
                if(!sort.equals("topRated")){
                    progress = new ProgressDialog(this);
                    progress.setTitle("");
                    progress.setMessage("Getting Movies...");
                    progress.setCancelable(false);
                    progress.show();
                    sort="topRated";
                    loaderManager.destroyLoader(LOADER_ID);
                    loaderManager.initLoader(LOADER_ID, null , this);
                    myToolbar.setTitle("Top Rated Movies");
                    //new Fetch(this , sort).execute();
                }

                return  true;
            case R.id.favourites:
                if(!sort.equals("favourites"))
                {
                    sort="favourites";
                    loaderManager.destroyLoader(LOADER_ID);
                    loaderManager.initLoader(LOADER_ID, null , this);
                    myToolbar.setTitle("Favourites");
                    //new Fetch(this , sort).execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Movie choosen = movies.get(clickedItemIndex);
        Intent intent = new Intent(this , MovieDetails.class);
        intent.putExtra(EXTRA_Title , choosen.getOriginalTitle());
        intent.putExtra(EXTRA_Overview , choosen.getOverview());
        intent.putExtra(EXTRA_ReleaseDate , choosen.getReleaseDate());
        intent.putExtra(EXTRA_UserRate , choosen.getVoteAverage());
        intent.putExtra(EXTRA_PosterPath , choosen.getPosterPath());
        intent.putExtra(EXTRA_MOVIEID , choosen.getMovie_ID());
        intent.putExtra(EXTRA_SORTMETHOD , sort);
        intent.putExtra(EXTRA_BACKDROPPATH , choosen.getBackdrop_path());
        startActivity(intent);
    }

    public static ArrayList<Movie> convertCursorToArrayList(Cursor cursor){
        int pos = 0;
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.moveToPosition(pos)){
            Movie movie = new Movie();
            movie.setMovie_ID(cursor.getString(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_ID)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_TITLE)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE)));
            movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_RATE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));


            byte[] byteArray =cursor.getBlob(cursor.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER));
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
            movie.setPoster(bitmap);

            movies.add(movie);
            pos++;
        }
        return movies;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<  ArrayList<Movie>  >(this){
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                movies = new ArrayList<>();
                try {
                    if(sort.equals("popular")){
                        movies = NetworkUtilities.getPopularMovies();
                    }
                    else if(sort.equals("topRated")) {
                        movies = NetworkUtilities.getTopRatedMovies();
                    }else if(sort.equals("favourites")){
                        Cursor cursor = getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI ,
                                null, null , null , null);
                        movies = convertCursorToArrayList(cursor);
                    }
                    return movies;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if(sort.equals("favourites") && (movies == null || movies.size()== 0)){
            setContentView(R.layout.activity_main_connection_problem);
            problemDescription = (TextView)findViewById(R.id.connectivity_problem_text_view);
            problemDescription.setText("Favourites list is empty");
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            findViewById(R.id.ret).setVisibility(View.GONE);
            progress.dismiss();

            switch(sort){
                case "popular":
                    myToolbar.setTitle("Popular Movies");
                    break;
                case "topRated":
                    myToolbar.setTitle("Top Rated Movies");
                    break;
                case "favourites":
                    myToolbar.setTitle("Favourites");
                    break;
            }
            return;
        }
        if( movies == null || movies.size()== 0){
            setContentView(R.layout.activity_main_connection_problem);
            problemDescription = (TextView)findViewById(R.id.connectivity_problem_text_view);
            problemDescription.setText(R.string.no_network);
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            progress.dismiss();
        }else {
            setContentView(R.layout.activity_main);
            recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
            myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            //movies = new ArrayList<>();
            int numOfCols=0;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                numOfCols = 3;
            else
                numOfCols=2;
            layoutManager = new GridLayoutManager(this ,numOfCols);

            recyclerView.setLayoutManager(layoutManager);
            adapter = new MovieAdapter(movies,this , (MainActivity)this , sort );
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            progress.dismiss();
        }
        switch(sort){
            case "popular":
                myToolbar.setTitle("Popular Movies");
                break;
            case "topRated":
                myToolbar.setTitle("Top Rated Movies");
                break;
            case "favourites":
                myToolbar.setTitle("Favourites");
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }



    public void reload(View v){
        recreate();
    }
}
