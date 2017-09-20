package com.solutionco.android.movies.Data;

/**
 * Created by Ahmed on 4/9/2017.
 */

public class Review {
    private String review_ID;
    private String author;
    private String review_content;

    public String getReview_ID() {
        return review_ID;
    }

    public void setReview_ID(String review_ID) {
        this.review_ID = review_ID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

}
