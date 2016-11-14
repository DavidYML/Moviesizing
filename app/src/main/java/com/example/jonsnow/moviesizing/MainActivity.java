package com.example.jonsnow.moviesizing;

import android.annotation.TargetApi;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
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

import static android.R.attr.id;
import static com.example.jonsnow.moviesizing.R.*;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppController.OnMovieListChangedListener, SearchView.OnQueryTextListener {


    private static final String KEY_RECYCLER_STATE = "recyclerState";
    private RecyclerView recyclerView;
    private Configuration configuration;
    MovieListAdapter movieListAdapter;
    private ArrayAdapter<String> listAdapter;
    private Bundle mBundleRecyclerViewState;

    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerScrollListener endlessScrollListener;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );


        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();
            }
        } );


        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

//        listView = (ListView) findViewById(R.id.ListView);
//        listAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, AppController.getmInstance().getList());
//        listView.setAdapter(listAdapter);

        recyclerView = (RecyclerView) findViewById( R.id.recycleView );

        //change the view of the recycleView

        linearLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );
        movieListAdapter = new MovieListAdapter( this, AppController.getmInstance().getMovieList() );
        recyclerView.setAdapter( movieListAdapter );


        //todo add OnClickListener


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory( true )
                .cacheOnDisk( true )
//                .showImageForEmptyUri(R.drawable.ic_cry_face)
//                .showImageOnLoading(R.drawable.ic_cry_face)
                .displayer( new FadeInBitmapDisplayer( 500 ) )
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder( getApplicationContext() )
                .defaultDisplayImageOptions( defaultOptions )
                .denyCacheImageMultipleSizesInMemory()
                .build();

        ImageLoader.getInstance().init( config );

//        Onderaan zorgt voor eindeloze scrollen !!!!
//          endlessScrollListener = new EndlessRecyclerScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                AppController.getmInstance().loadSearchMovieInfoByPage(page);
//            }
//        };
//        // Adds the scroll listener to RecyclerView
//        recyclerView.addOnScrollListener(endlessScrollListener);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppController.getmInstance().addOnMovieListChangedListener( this );
//        movieListAdapter.notifyDataSetChanged();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable( KEY_RECYCLER_STATE );
            recyclerView.getLayoutManager().onRestoreInstanceState( listState );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable( KEY_RECYCLER_STATE, listState );

        AppController.getmInstance().removeMovieListChangedListener( this );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


//        return true;

        getMenuInflater().inflate( R.menu.main, menu );

        MenuItem menuItem = menu.findItem( R.id.searchView );

        SearchView searchView = (SearchView) MenuItemCompat.getActionView( menuItem );

        searchView.setIconifiedByDefault( false );
        searchView.setOnQueryTextListener( this );

        MenuItemCompat.setOnActionExpandListener( menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                AppController.getmInstance().fetchMovieInfo();

                return true;
            }
        } );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement




//        if (id == R.id.action_settings) {
//            return true;
//        }








        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            AppController.getmInstance().fetchMovieInfo();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    public void onMovieListChanged() {
        movieListAdapter.notifyDataSetChanged();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable( KEY_RECYCLER_STATE );
            recyclerView.getLayoutManager().onRestoreInstanceState( listState );
        }


        class EndlessRecyclerScrollListener {


        }
    }

    @Override
    public boolean onCreateOptions(Menu menu) {
        return false;
    }


    @Override
         public boolean onQueryTextSubmit (String query){
            System.out.println("Submitted Search"+ query);
                    AppController.getmInstance().searchMovieByTitle( query );
            return false;

        }

                 @Override
         public boolean onQueryTextChange (String newText){
             return false;

        }


    }











