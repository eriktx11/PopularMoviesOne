package com.example.android.popularmoviesone;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.android.popularmoviesone.data.MovieContract;
import com.example.android.popularmoviesone.sync.MovieSyncAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainGrid extends ActionBarActivity implements ForecastFragment.Callback {

    private final String LOG_TAG = MainGrid.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    DetailFragment.fromDetailDataInterface dataDetailCall;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean res = getResources().getBoolean(R.bool.isTab);
        setContentView(R.layout.main_grid);

        //containerB = (ViewGroup) this.findViewById(R.id.fragment_forecast);
        //containerA = (ViewGroup) this.findViewById(R.id.movie_detail_container);

        //getResources().getBoolean(R.bool.isTab)
        //findViewById(R.id.movie_detail_container) != null

        if (!res) {

            mTwoPane = true;


            dataDetailCall = new DetailFragment.fromDetailDataInterface() {
                @Override
                public void onArticleSelected(Uri position) {

                }
            };

            if (savedInstanceState == null) {

                DetailFragment detailFragment = ((DetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.detail_fragment));
                detailFragment.setCallForDetail(this.dataDetailCall);

            }
    }

    else

    {
        mTwoPane = false;
        getSupportActionBar().setElevation(0f);
    }

    ForecastFragment forecastFragment = ((ForecastFragment) getSupportFragmentManager()
            .findFragmentById(R.id.fragment_forecast));

    forecastFragment.setUseTodayLayout(!mTwoPane);

    MovieSyncAdapter.initializeSyncAdapter(this);

}


    @Override
    protected void onResume() {
        super.onResume();
        //String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager

            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);

            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);

        }


    @Override
    public void onItemSelected(Uri contentUri) {



        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }

}

