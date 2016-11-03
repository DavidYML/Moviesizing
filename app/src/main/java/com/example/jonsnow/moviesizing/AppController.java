package com.example.jonsnow.moviesizing;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.config.Configuration;
import com.omertron.themoviedbapi.model.discover.Discover;
import com.omertron.themoviedbapi.model.movie.MovieBasic;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonsnow on 25/10/16.
 */

public class AppController extends Application {


    private static AppController Instance;
    private List<MovieBasic> movieList = new ArrayList<>();
    private TheMovieDbApi api;
    private Configuration configuration;

    public static synchronized AppController getmInstance() {
        return Instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;

        try {
            api = new TheMovieDbApi( "91081cb90d79ffe81d5fdd28954a245a" );
            FetchConfiguration fetchConfiguration = new FetchConfiguration();
            fetchConfiguration.execute();
            FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
            fetchMovieInfo.execute();

        } catch (MovieDbException e) {
            e.printStackTrace();
        }

    }



    private List<OnMovieListChangedListener> allListeners = new ArrayList<>();

    public List<MovieBasic> getMovieList() {
        return movieList;
    }

    public void addOnMovieListChangedListener(OnMovieListChangedListener listener) {
        allListeners.add(listener);
    }

    public void removeMovieListChangedListener(OnMovieListChangedListener listener) {
        allListeners.remove(listener);
    }

    public interface OnMovieListChangedListener {
        void onMovieListChanged();
    }
    public void notifyAllListeners() {
        for (OnMovieListChangedListener listener : allListeners) {
            listener.onMovieListChanged();
        }
    }
    private class FetchConfiguration extends AsyncTask<Void, Void, Configuration> {
        @Override
        protected Configuration doInBackground(Void...params) {
            try {
                return api.getConfiguration();

            }catch (MovieDbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Configuration configuration) {
            super.onPostExecute(configuration);
            AppController.this.configuration = configuration;
        }
    }
    private  class FetchMovieInfo extends AsyncTask<Void, Void, ResultList<MovieBasic>> {

        @Override
        protected ResultList<MovieBasic> doInBackground(Void...params) {
            try {
                return api.getDiscoverMovies(new Discover());
            } catch (MovieDbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultList<MovieBasic> movieBasicResultList) {
            super.onPostExecute(movieBasicResultList);

            Log.v("Found:", movieBasicResultList.toString());
//            URL imageUrl = configuration.createImageUrl(movieBasicResultList.getResults().get(0).getBackdropPath(),"w780");



            movieList.clear();

            movieList.addAll(movieBasicResultList.getResults());
            notifyAllListeners();



        }


    }
    private class FetchMovieById extends AsyncTask<Integer, Void, MovieInfo> {


        @Override
        protected MovieInfo doInBackground(Integer... params) {
            try {
                return api.getMovieInfo(params[0].intValue(),"en");
            } catch (MovieDbException e) {
                e.printStackTrace();
            } return null;

        }

        @Override
        protected void onPostExecute(MovieInfo movieInfo) {
            super.onPostExecute(movieInfo);
            Intent intent = new Intent(AppController.this, MovieDetailActivity.class);
            intent.putExtra("TheMovie",movieInfo);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public void fetchMovieById (int movieId) {
        new FetchMovieById().execute(movieId);
    }

    //todo fetch movie by id do in backgr, op postExec (intent naar volgend scherm)

    public URL createImageUrl (String imagePath, String size) {
        try {
            return configuration.createImageUrl(imagePath,size);
        } catch (MovieDbException e) {
            e.printStackTrace();
            return null;
        }
    }

}
