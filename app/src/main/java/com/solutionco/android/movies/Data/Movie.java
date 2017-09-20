package com.solutionco.android.movies.Data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ahmed on 2/28/2017.
 */

public class Movie {
    private String movie_ID;
    private String originalTitle;
    private String posterPath;
    private Bitmap poster;
    private Bitmap backPoster;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private boolean favourite;
    private String backdrop_path;


    public Bitmap getBackPoster() {
        return backPoster;
    }

    public void setBackPoster(Bitmap backPoster) {
        this.backPoster = backPoster;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getMovie_ID() {
        return movie_ID;
    }

    public void setMovie_ID(String movie_ID) {
        this.movie_ID = movie_ID;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }


    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
