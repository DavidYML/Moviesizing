package com.example.jonsnow.moviesizing;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.EventLogTags;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.omertron.themoviedbapi.model.media.RatedValue;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.movie.ReleaseDate;

import java.net.URL;

import static com.example.jonsnow.moviesizing.R.id.imageView;
import static com.example.jonsnow.moviesizing.R.id.title;


public class MovieDetailActivity extends AppCompatActivity {


    private ImageView imageViewDetails;
    private TextView descriptionDetail;
    private TextView title;
    private TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_detail );

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled( true );

        MovieInfo theMovie = (MovieInfo) getIntent().getExtras().get( "TheMovie" );


        System.out.println( theMovie );

        imageViewDetails = (ImageView) findViewById( R.id.imageViewDetails );
        descriptionDetail = (TextView) findViewById( R.id.descriptionDetail );
        title = (TextView) findViewById( R.id.title );
        rating = (TextView) findViewById( R.id.rating );

        URL imageUrl = null;


        imageUrl = AppController.getmInstance().createImageUrl( theMovie.getBackdropPath(), "w500" );
        ImageLoader.getInstance().displayImage( imageUrl.toString(), imageViewDetails );


        descriptionDetail.setText( theMovie.getOverview() );
        title.setText( theMovie.getTitle() );
        rating.setText( "   Rating : " + theMovie.getVoteAverage() + "/10 (" + theMovie.getVoteCount() + ")" );


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (
        item.getItemId() == android.R.id.home
        ){
            NavUtils.navigateUpFromSameTask( this );

        } return super.onOptionsItemSelected( item );
        }
    }
