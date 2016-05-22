package com.example.android.popularmoviesone;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.android.popularmoviesone.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;

/**
 * Created by erikllerena on 4/29/16.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private String mForecast;
    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            MovieContract.TheMovieList.TABLE_NAME + "." + MovieContract.TheMovieList._ID,
            MovieContract.TheMovieList.C_POPULAR,
            MovieContract.TheMovieList.C_TOP_RATED,
            MovieContract.TheMovieList.C_MOVIE_ID,
            MovieContract.TheMovieList.C_POSTER_PATH,
            MovieContract.TheMovieList.C_RELEASE_D,
            MovieContract.TheMovieList.C_TITLE,
            MovieContract.TheMovieList.C_RATING,
            MovieContract.TheMovieList.C_OVERVIEW,
            MovieContract.TheMovieList.C_FAV,
            MovieContract.TheMovieExtras.C_CONTENT,
            MovieContract.TheMovieExtras.C_AUTHOR,
            MovieContract.TheMovieExtras.C_TRAILER_KEY
    };

    public static final int COL_THE_MOVIES_ID = 0;
    public static final int COL_POPULAR = 1;
    public static final int COL_TOP_RATED = 2;
    public static final int COL_MOVIE_ID = 3;
    public static final int COL_POSTER_PATH = 4;
    public static final int COL_RELEASE_D = 5;
    public static final int COL_TITTLE = 6;
    public static final int COL_RATING = 7;
    public static final int COL_OVERVIEW = 8;
    public static final int C_FAV = 9;

    public static final int COL_CONTENT = 10;
    public static final int COL_AUTHOR = 11;
    public static final int COL_TRAILER_KEY = 12;

    private TextView OverviewTextView;
    private TextView textTitle;
    private TextView textRate;
    private TextView textDate;
    private ImageView imageView;
    private VideoView VideoTrailer;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.movie_id_detail);
        OverviewTextView = (TextView) rootView.findViewById(R.id.movie_text_detail);
        textTitle = (TextView) rootView.findViewById(R.id.MovieTitle);
        textRate = (TextView) rootView.findViewById(R.id.MovieRating);
        textDate = (TextView) rootView.findViewById(R.id.MovieReleaseDate);
        VideoTrailer = (VideoView) rootView.findViewById(R.id.videoView);

        return rootView;


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Read weather condition ID from cursor
            int movieId = data.getInt(COL_MOVIE_ID);


            Picasso.with(getContext()).load(COL_POSTER_PATH).into(imageView);

            // Read description from cursor and update view
            String overview = data.getString(COL_OVERVIEW);
            OverviewTextView.setText(overview);

            String title = data.getString(COL_TITTLE);
            textTitle.setText(title);

            String date = data.getString(COL_RELEASE_D);
            textDate.setText(date);

            String rate = data.getString(COL_RATING);
            textRate.setText(rate);

            String trailer = "https://youtu.be/"+data.getString(COL_TRAILER_KEY);
            Uri uri = Uri.parse(trailer);
            VideoTrailer.setVideoURI(uri);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}
