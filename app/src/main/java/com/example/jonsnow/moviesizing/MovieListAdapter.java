package com.example.jonsnow.moviesizing;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.movie.MovieBasic;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonsnow on 24/10/16.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {


    private Context context;
    private List<MovieBasic> movieList ;

    public MovieListAdapter(Context context, List<MovieBasic> movieList) {
        this.context = context;
        this.movieList = movieList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //load xml and create ViewHolder

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieBasic currentMovie = movieList.get(position);
        holder.getTitleTextView().setText(currentMovie.getTitle());
        holder.getRatingTextView().setText(""+ currentMovie.getVoteAverage());
        holder.getDescriptionTextView().setText(currentMovie.getOverview());
//        holder.getImageView().setImageResource(currentMovie.g());

            URL imageUrl = AppController.getmInstance().createImageUrl(currentMovie.getPosterPath(),"w92");
            ImageLoader.getInstance().displayImage(imageUrl.toString(),holder.getImageView());
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getmInstance().fetchMovieById(currentMovie.getId());
            }
        });

        }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void insertMovie(int position, MovieBasic movie) {
        movieList.add(position,movie);
        notifyItemInserted(position);

    }

    public  void removeMovie(MovieBasic movie) {
        int position = movieList.indexOf(movie);
        movieList.remove(position);
        notifyItemRemoved(position);
    }






}


