package com.solutionco.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutionco.android.movies.Data.Review;
import com.solutionco.android.movies.Data.Trailer;

import java.util.ArrayList;

/**
 * Created by Ahmed on 4/10/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{
    ArrayList<Review> reviews ;
    Context context;

    public ReviewsAdapter(Context con , ArrayList<Review>t ) {
        context = con;
        reviews = t;
    }

    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.review_row;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id , parent ,false);
        ReviewsAdapter.ReviewViewHolder viewHolder = new ReviewsAdapter.ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public  class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView reviewerName;
        TextView review_content;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerName = (TextView) itemView.findViewById(R.id.reviwer_name);
            review_content = (TextView) itemView.findViewById(R.id.review_content);
        }

        void bind(int listIndex) {
            reviewerName.setText(reviews.get(listIndex).getAuthor());
            review_content.setText(reviews.get(listIndex).getReview_content());
        }
        @Override
        public void onClick(View v) {

        }
    }
}
