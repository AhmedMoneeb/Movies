package com.solutionco.android.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutionco.android.movies.Data.Trailer;

import java.util.ArrayList;

/**
 * Created by Ahmed on 4/9/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    ArrayList<Trailer> trailers;
    Context context;
    private listItemClickListener listener;

    public TrailersAdapter(Context con, ArrayList<Trailer> t, listItemClickListener l) {
        context = con;
        trailers = t;
        listener = l;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.trailer_row;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public interface listItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailernumber;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailernumber = (TextView) itemView.findViewById(R.id.traileridnumber);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            trailernumber.setText("Trailer " + (listIndex + 1));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
        }
    }

}
