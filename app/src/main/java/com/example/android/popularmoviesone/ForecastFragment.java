package com.example.android.popularmoviesone;

/**
 * Created by erikllerena on 4/29/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.popularmoviesone.data.MovieContract;
import com.example.android.popularmoviesone.data.MovieProvider;
import com.example.android.popularmoviesone.sync.MovieSyncAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAdapter mForecastAdapter;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";
    SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private int SELECT = 0;

    private static final int FORECAST_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {

            MovieContract.TheMovieList.TABLE_NAME + "." + MovieContract.TheMovieList._ID,
            MovieContract.TheMovieList.C_OVERVIEW,
            MovieContract.TheMovieList.C_RATING,
            MovieContract.TheMovieList.C_RELEASE_D,
            MovieContract.TheMovieList.C_MOVIE_ID,
            MovieContract.TheMovieList.C_TITLE,
            MovieContract.TheMovieList.C_POPULAR,
            MovieContract.TheMovieList.C_TOP_RATED,
            MovieContract.TheMovieList.C_FAV,
            MovieContract.TheMovieList.C_POSTER_PATH,

    };



    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_OVERVIEW = 1;
    static final int COL_RATING = 2;
    static final int COL_RELEASE_D = 3;
    static final int COL_MOVIE_ID = 4;
    static final int COL_TITLE = 5;
    static final int COL_POPULAR = 6;
    static final int COL_TOP_RATED = 7;
    static final int COL_FAV = 8;
    static final int COL_POSTER_PATH = 9;



    public interface Callback {

        public void onItemSelected(Uri dateUri);
    }

    public ForecastFragment() {
    }

    private ArrayList<GridItem> mGridData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        mForecastAdapter.notifyDataSetChanged();
        Cursor cursor = (Cursor) mGridView.getItemAtPosition(0);

        Uri movieSelectUri = MovieContract.TheMovieList.buildForPopular(
                "popular");

        String SELECTION="";
        List<String> selectionArgs = new ArrayList<String>();

        final String[] SELECTION_ARGS = new String[selectionArgs.size()];

        selectionArgs.add("1");
        selectionArgs.toArray(SELECTION_ARGS);

        int id = item.getItemId();
        if (id == R.id.sortP) {
           // openMovies();
            SELECT = 1;
            //mEditor.putInt("select", SELECT);
            //syncAfter(SELECT);
            //mEditor.apply();
            //bundle.putInt("select", 1);
            //return true;


                    SELECTION = "popular=?";

             new CursorLoader(getActivity(),
                    movieSelectUri,
                    FORECAST_COLUMNS,
                    SELECTION,
                    SELECTION_ARGS,
                    null
            );

        }

        if (id == R.id.sortR) {
            SELECT = 2;
            //mEditor.putInt("select", SELECT);
            //syncAfter(SELECT);
            //mEditor.apply();
            //bundle.putInt("select", 1);
            //return true;


            SELECTION = "top_rated=?";

            new CursorLoader(getActivity(),
                    movieSelectUri,
                    FORECAST_COLUMNS,
                    SELECTION,
                    SELECTION_ARGS,
                    null
            );
        }


        MovieSyncAdapter.syncImmediately(getActivity(), SELECT);
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        return super.onOptionsItemSelected(item);
    }

    private Uri uriOutgoing;
    //private MovieAdapter mGridAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSettings.edit();
        mEditor.apply();

        Context context;
        context = getContext();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // initialize our MovieAdapter
        mForecastAdapter = new MovieAdapter(getActivity(), null, 0);
        // initialize mGridView to the GridView in fragment_main.xml

        mGridView = (GridView) rootView.findViewById(R.id.movieGrid);


        mGridView.setAdapter(mForecastAdapter);



        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {
                        ((Callback) getActivity())
                                .onItemSelected(MovieContract.TheMovieList.buildMovieUri(cursor.getString(COL_MOVIE_ID)));

                    }
                    mPosition = position;
                }
        });



        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {

            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the location when we create the loader, all we need to do is restart things
//    void onLocationChanged( ) {
//        openMovies(1);
//        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
//    }


    private void onLocationChanged(){

    //getLoaderManager().initLoader(FORECAST_LOADER, null, this);
    //mForecastAdapter.notifyDataSetChanged();
      //  mGridView.setAdapter(mForecastAdapter);

        openMovies(SELECT);
    getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    private void openMovies(int UserSelect) {

        MovieSyncAdapter.syncImmediately(getActivity(), UserSelect);
//        Uri movieSelectUri = MovieContract.TheMovieList.buildForPopular(
//                "selection");
//
//        String SELECTION="";
//        List<String> selectionArgs = new ArrayList<String>();
//
//        final String[] SELECTION_ARGS = new String[selectionArgs.size()];
//
//        int select = mSettings.getInt("option", SELECT);
//
//        if(select!=0) {
//            selectionArgs.add("1");
//            selectionArgs.toArray(SELECTION_ARGS);
//            switch (select) {
//                case 1:
//                    SELECTION = "popular=?";
//                    break;
//                case 2:
//                    SELECTION = "top_rated=?";
//                    break;
//
//            }
//            select=0;
//
//            return new CursorLoader(getActivity(),
//                    movieSelectUri,
//                    FORECAST_COLUMNS,
//                    SELECTION,
//                    SELECTION_ARGS,
//                    null
//            );
//
//
//        }else {
//
//            return new CursorLoader(getActivity(),
//                    movieSelectUri,
//                    FORECAST_COLUMNS,
//                    null,
//                    null,
//                    null
//            );
//        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri weatherForLocationUri = MovieContract.TheMovieList.buildForPopular(
                "popular");

//        String SELECTION="";
//        List<String> selectionArgs = new ArrayList<String>();
//
//
//
//        final String[] SELECTION_ARGS = new String[selectionArgs.size()];
//
//
//        int select = mSettings.getInt("option", SELECT);
//
//        if(select!=0) {
//            selectionArgs.add("1");
//            selectionArgs.toArray(SELECTION_ARGS);
//            switch (select) {
//                case 1:
//                    SELECTION = "popular=?";
//                    break;
//                case 2:
//                    SELECTION = "top_rated=?";
//                    break;
//
//            }
//
//
//            return new CursorLoader(getActivity(),
//                    weatherForLocationUri,
//                    FORECAST_COLUMNS,
//                    SELECTION,
//                    SELECTION_ARGS,
//                    null
//            );


       // }else {

            return new CursorLoader(getActivity(),
                    weatherForLocationUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null
            );
        //}


//        if(bundle.getInt("select")==1){
//            SELECTION = "popular=?";
//        }
//        if(bundle.getInt("select")==2){
//            SELECTION = "top_rated=?";
//        }

    }




    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter= null;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}