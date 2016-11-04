package com.example.jonsnow.moviesizing;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.omertron.themoviedbapi.model.artwork.Artwork;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.net.URL;
import java.util.List;


public class MovieDetailActivity extends AppCompatActivity implements AppController.OnMovieListChangedListener {


    private ImageView imageViewDetails;
    private TextView descriptionDetail;
    private TextView title;
    private TextView rating;
    private TextView runTime;
    private TextView budget;
    private LinearLayout cast;
    private TextView releaseDate;
    private List<Artwork> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_detail );

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeButtonEnabled(true);

        MovieInfo theMovie = (MovieInfo) getIntent().getExtras().get( "TheMovie" );


        System.out.println( theMovie );

        imageViewDetails = (ImageView) findViewById( R.id.imageViewDetails );
        descriptionDetail = (TextView) findViewById( R.id.descriptionDetail );
        title = (TextView) findViewById( R.id.title );
        rating = (TextView) findViewById( R.id.rating );
        runTime = (TextView) findViewById( R.id.runTime );
        budget = (TextView) findViewById( R.id.budget );
        cast = (LinearLayout) findViewById( R.id.cast );
        releaseDate = (TextView) findViewById( R.id.releaseDate );


        URL imageUrl = null;


        imageUrl = AppController.getmInstance().createImageUrl( theMovie.getBackdropPath(), "w1280" );
        ImageLoader.getInstance().displayImage( imageUrl.toString(), imageViewDetails );


        descriptionDetail.setText( theMovie.getOverview() );
        title.setText( theMovie.getTitle() );
        rating.setText( "   Rating : " + theMovie.getVoteAverage() + "/10 (" + theMovie.getVoteCount() + ")" );
        runTime.setText( "Movie length: " + theMovie.getRuntime() + " min" );
        budget.setText( "Movie Budget: " + theMovie.getBudget() + " $" );
        releaseDate.setText( "Release Date : " + theMovie.getReleaseDate() );


//        List<MediaCreditCast> castList = null;
//
        AppController.getmInstance().createCastList( theMovie.getId() );


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getmInstance().addOnMovieListChangedListener( this );
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppController.getmInstance().removeMovieListChangedListener( this );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (
                item.getItemId() == android.R.id.home
                ) {
            NavUtils.navigateUpFromSameTask( this );

        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onMovieListChanged() {
        for (MediaCreditCast castMember : AppController.getmInstance().getCastList()) {
            if (
                    castMember.getArtworkPath() != null) {
                LinearLayout memberLayout = new LinearLayout( this );
                memberLayout.setOrientation( LinearLayout.VERTICAL );
                ImageView castImage = new ImageView( this );
                castImage.setLayoutParams( new LinearLayout.LayoutParams( 500, 500 ) );
                castImage.setScaleType( ImageView.ScaleType.FIT_CENTER );
                ImageLoader.getInstance().displayImage( AppController.getmInstance().createImageUrl( castMember.getArtworkPath(), "w185" ).toString(), castImage );
                memberLayout.addView( castImage );
                TextView castName = new TextView( this );
                castName.setText( castMember.getName() );
                System.out.println(castMember.getName());
                memberLayout.addView( castName );
                castName.setTextSize( 10 );
                castName.setTextAlignment( LinearLayout.TEXT_ALIGNMENT_CENTER );
                cast.addView( memberLayout );

            }
        }
    }
}
