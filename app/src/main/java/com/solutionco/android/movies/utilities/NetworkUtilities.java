package com.solutionco.android.movies.utilities;

import com.solutionco.android.movies.Data.Movie;
import com.solutionco.android.movies.Data.Review;
import com.solutionco.android.movies.Data.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ahmed on 2/28/2017.
 */
final public class NetworkUtilities {

    private static final String API_KEY = "Put your API_key here";
    private static String topRatedURL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    private static String popularURL = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

    private static String trailerURL = "http://api.themoviedb.org/3/movie/";


    public static ArrayList<Movie> getPopularMovies() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
        .url(popularURL)
        .build();

        Response response = client.newCall(request).execute();
        return pasrseJSON(response.body().string());

    }
    public static ArrayList<Movie> getTopRatedMovies() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(topRatedURL)
                .build();

        Response response = client.newCall(request).execute();
        return pasrseJSON(response.body().string());

    }

    public static ArrayList<Movie>pasrseJSON (String response){
        ArrayList<Movie> res = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Movie movie = new Movie();
                JSONObject jsonobject = array.getJSONObject(i);

                movie.setOriginalTitle(jsonobject.getString("original_title"));
                movie.setOverview(jsonobject.getString("overview"));
                movie.setReleaseDate(jsonobject.getString("release_date"));
                movie.setPosterPath("http://image.tmdb.org/t/p/"+"w342"+jsonobject.getString("poster_path"));
                movie.setVoteAverage(jsonobject.getString("vote_average"));
                movie.setMovie_ID(jsonobject.getString("id"));
                movie.setBackdrop_path("http://image.tmdb.org/t/p/"+"w342"+jsonobject.getString("backdrop_path"));
                res.add(movie);
            }
        }catch (Exception e){

        }

        return res;
    }

    public static ArrayList<Trailer> getTrailers(String movieID) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(trailerURL+movieID+"/videos?api_key="+API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        return pasrseTrailersJSON(response.body().string());
    }


    public static ArrayList<Trailer> pasrseTrailersJSON (String response){
        ArrayList<Trailer> res = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Trailer trailer = new Trailer();
                JSONObject jsonobject = array.getJSONObject(i);

                trailer.setSite(jsonobject.getString("site"));
                trailer.setTrailer_ID(jsonobject.getString("id"));
                trailer.setKey(jsonobject.getString("key"));
                res.add(trailer);
            }
        }catch (Exception e){

        }

        return res;
    }


    public static ArrayList<Review> getReviews(String movieID) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(trailerURL+movieID+"/reviews?api_key=" + API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        return pasrseReviewsJSON(response.body().string());
    }

    public static ArrayList<Review> pasrseReviewsJSON (String response){
        ArrayList<Review> res = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Review review = new Review();
                JSONObject jsonobject = array.getJSONObject(i);

                review.setAuthor(jsonobject.getString("author"));
                review.setReview_content(jsonobject.getString("content"));
                review.setReview_ID(jsonobject.getString("id"));
                res.add(review);
            }
        }catch (Exception e){

        }

        return res;
    }
}
