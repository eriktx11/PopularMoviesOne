package com.example.android.popularmoviesone;

/**
 * Created by erikllerena on 4/29/16.
 */

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.popularmoviesone.data.DataBaseHelper;
import com.example.android.popularmoviesone.data.MovieContract;
import com.example.android.popularmoviesone.data.MovieProvider;
import com.example.android.popularmoviesone.sync.MovieSyncAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAdapter mForecastAdapter;

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";
    //private static final String BROWSE_OUT = "savedView";

    //private String SELECT="1";

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

    private SharedPreferences sPrefs;
    SharedPreferences.Editor editor;

    private AppPreferences _appPrefs;


    public interface Callback {

        public void onItemSelected(Uri movieUri); //was dateUri
    }


    public ForecastFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //editor = sPrefs.edit();
        //editor.putString("select", SELECT);
        //editor.apply();

        _appPrefs = new AppPreferences(getContext());

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    //menu select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.sortP) {
            //SELECT = "1";
//            editor.putString("select", SELECT);
//            editor.apply();
            _appPrefs.saveIntVal(1);
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
            MovieSyncAdapter.syncImmediately(getActivity(), 1);

        }

        if (id == R.id.sortR) {
//            SELECT = "2";
//            editor.putString("select", SELECT);
//            editor.apply();
            _appPrefs.saveIntVal(2);
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
            MovieSyncAdapter.syncImmediately(getActivity(), 2);
        }


        if (id == R.id.favs) {
//            SELECT = "3";
//            editor.putString("select", SELECT);
//            editor.apply();

            _appPrefs.saveIntVal(3);

            ContentValues whichCol = new ContentValues();
            whichCol.put(MovieContract.TheMovieList.C_FAV, "0");
            getContext().getContentResolver().update(MovieContract.TheMovieList.CONTENT_URI, whichCol, null, null);


            Map<String, ?> allPrefs = _appPrefs.getAll();
            Set<String> set = allPrefs.keySet();
            for (String extractedMovieId : set) {

                whichCol.put(MovieContract.TheMovieList.C_FAV, "1");
                String[] arg = {extractedMovieId};
                String sel = MovieContract.TheMovieList.C_MOVIE_ID + "=?";
                getContext().getContentResolver().update(MovieContract.TheMovieList.CONTENT_URI, whichCol, sel, arg);
            }

            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);


//            Uri moviesUri = MovieContract.TheMovieList.buildForPopular();
//            String[] arg = {"1"};
//            String sel = MovieContract.TheMovieList.C_FAV + "=?";
//
//            Cursor data = getActivity().getContentResolver().query(moviesUri,
//                    FORECAST_COLUMNS,
//                    sel,
//                    arg,
//                    null);
//
//            mForecastAdapter.swapCursor(data);
        }

        return super.onOptionsItemSelected(item);
    }

    private Uri uriOutgoing;
    //private MovieAdapter mGridAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mForecastAdapter = new MovieAdapter(getActivity(), null, 1);
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

    @Override
    public void onResume() {

        int returning;

        returning = _appPrefs.getIngBody();
                //sPrefs.getString("select","");

        String routing;

        switch (returning){
//
//            case 1:
//                routing = MovieContract.TheMovieList.C_POPULAR+"=?";
//                break;
//            case 2:
//                routing = MovieContract.TheMovieList.C_TOP_RATED+"=?";
//                break;
            case 3: {
                //routing = MovieContract.TheMovieList.C_FAV + "=?";

                ContentValues whichCol = new ContentValues();
                whichCol.put(MovieContract.TheMovieList.C_FAV, "0");
                getContext().getContentResolver().update(MovieContract.TheMovieList.CONTENT_URI, whichCol, null, null);


                Map<String, ?> allPrefs = _appPrefs.getAll();
                Set<String> set = allPrefs.keySet();
                for (String extractedMovieId : set) {

                    whichCol.put(MovieContract.TheMovieList.C_FAV, "1");
                    String[] arg = {extractedMovieId};
                    String sel = MovieContract.TheMovieList.C_MOVIE_ID + "=?";
                    getContext().getContentResolver().update(MovieContract.TheMovieList.CONTENT_URI, whichCol, sel, arg);
                }
            }
//                break;
//            default:
//                routing = MovieContract.TheMovieList.C_POPULAR+"=?";
        }
//
//
        Uri moviesUri = MovieContract.TheMovieList.buildForPopular();
        String[] arg = {"1"};
        routing = MovieContract.TheMovieList.C_FAV + "=?"; //testing
        Cursor data = getActivity().getContentResolver().query(moviesUri,
                FORECAST_COLUMNS,
                routing,
                arg,
                null);

        mForecastAdapter.newView(getContext(), data, mGridView);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri moviesUri = MovieContract.TheMovieList.buildForPopular();

        String SELECTION = "popular=?";

        int select = _appPrefs.getIngBody();
                //sPrefs.getString("select", "");
        //int select = Integer.parseInt(selectR);
        String[] args = {"1"};
        //if (select != 0) {
        switch (select) {
            case 1:
                SELECTION = "popular=?";
                break;
            case 2:
                SELECTION = "top_rated=?";
                break;
            case 3:
                SELECTION = "fav_movie=?";
                break;
        }


        //if (select==1 || select==2) {
            return new CursorLoader(getActivity(),
                    moviesUri,
                    FORECAST_COLUMNS,
                    SELECTION,
                    args,
                    null
            );
        //}
        //else return null;


//        Uri moviesUri = MovieContract.TheMovieList.buildForPopular();
//        String[] arg = {"1"};
//
//        Cursor data = getActivity().getContentResolver().query(moviesUri,
//                FORECAST_COLUMNS,
//                routing,
//                arg,
//                null);


    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//        int sel = _appPrefs.getIngBody();
//        if(sel!=3)
//        {
            mForecastAdapter.swapCursor(data);
//        }
        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            mForecastAdapter = null;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}