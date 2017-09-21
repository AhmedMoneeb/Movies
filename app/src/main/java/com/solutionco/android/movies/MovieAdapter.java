package com.solutionco.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.solutionco.android.movies.Data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed on 2/28/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {


    private final ListItemClickListener clickListener;
    //ArrayList<String>paths ;
    String typeOfSort;
    Context context;
    ArrayList<Movie> movies;

    public MovieAdapter(ArrayList<Movie> movies, Context con, ListItemClickListener l, String s) {
        clickListener = l;
        context = con;
        typeOfSort = s;
        this.movies = movies;
       /* paths= new ArrayList<>();
        for(int i = 0 ; i<movies.size() ; i++){
            paths.add(movies.get(i).getPosterPath());
        }*/

    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id, parent, false);

        ImageViewHolder viewHolder = new ImageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public interface ListItemClickListener {
        public void onListItemClicked(int clickedItemIndex);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;

        public ImageViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if (typeOfSort.equals("favourites")) {
                moviePoster.setImageBitmap(movies.get(listIndex).getPoster());
                return;
            }
            Picasso.with(context)
                    .load(movies.get(listIndex).getPosterPath())
                    .into(moviePoster);
        }

        @Override
        public void onClick(View view) {
            int x = getAdapterPosition();
            clickListener.onListItemClicked(x);
        }
    }
}
