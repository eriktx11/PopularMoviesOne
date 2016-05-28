package com.example.android.popularmoviesone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
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
import com.example.android.popularmoviesone.interfaces.PosterExtrasAPI;
import com.example.android.popularmoviesone.models.ModelTrailerList;
import com.example.android.popularmoviesone.models.TrailerList;
import com.example.android.popularmoviesone.sync.MovieSyncAdapter;
import com.example.android.popularmoviesone.sync.PosterSyncAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by erikllerena on 4/29/16.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private String mForecast;
    private Uri mUri;
    private String extractedMovieId;

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
//            MovieContract.TheMovieExtras.C_CONTENT,
//            MovieContract.TheMovieExtras.C_AUTHOR,
//            MovieContract.TheMovieExtras.C_TRAILER_KEY
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
    private PosterExtrasAPI posterExtraAPI;

    private List<TrailerList> data;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            extractedMovieId = mUri.getPathSegments().get(2);
            //PosterSyncAdapter.syncImmediately(getActivity(), extractedMovieId);

            String movieUrl = "http://api.themoviedb.org/3/";
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(movieUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            posterExtraAPI = retrofit.create(PosterExtrasAPI.class);

            final Call<ModelTrailerList> MTrailerListCall = posterExtraAPI.getTrailerList(extractedMovieId, BuildConfig.OPEN_MOVIE_API_KEY);

            MTrailerListCall.enqueue(new Callback<ModelTrailerList>() {
                @Override
                public void onResponse(Call<ModelTrailerList> call, Response<ModelTrailerList> response) {
                    int statusCode = response.code();
                    ModelTrailerList trailerList = response.body();
                    Log.d("TrailerList", "onResponse: " + statusCode);

                    //Vector<ContentValues> cVVector = new Vector<ContentValues>(2);

                   // String key = trailerList;

                    //int i=0;

                    ContentValues movieParts = new ContentValues();

                    //movieParts.put(MovieContract.TheMovieExtras.C_TRAILER_KEY, key);
                    //movieParts.put(MovieContract.TheMovieList.C_TITLE, content);
                    //movieParts.put(MovieContract.TheMovieList.C_RELEASE_D, author);

                    //cVVector.add(movieParts);


                }
                @Override
                public void onFailure(Call<ModelTrailerList> call, Throwable t) {

                    Log.d("TrailerList", "onResponse: "+t.getMessage());
                }

            });


        }



        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.movie_id_detail);
        OverviewTextView = (TextView) rootView.findViewById(R.id.movie_text_detail);
        OverviewTextView.setMovementMethod(new ScrollingMovementMethod());
        textTitle = (TextView) rootView.findViewById(R.id.MovieTitle);
        textRate = (TextView) rootView.findViewById(R.id.MovieRating);
        textDate = (TextView) rootView.findViewById(R.id.MovieReleaseDate);
        VideoTrailer = (VideoView) rootView.findViewById(R.id.videoView);

        ImageView iv = (ImageView) rootView.findViewById(R.id.favbtn);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.fav_off);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 40, 40, true);
        iv.setImageBitmap(bMapScaled);

        ImageView playImg = (ImageView) rootView.findViewById(R.id.playbtn);
        Bitmap bMapPlay = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        Bitmap bMapPlayScaled = Bitmap.createScaledBitmap(bMapPlay, 60, 60, true);
        playImg.setImageBitmap(bMapPlayScaled);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            String poster = data.getString(COL_POSTER_PATH);

            poster = "http://image.tmdb.org/t/p/w185/"+poster;

            Picasso.with(getContext()).load(poster).into(imageView);

            // Read description from cursor and update view
            String overview = data.getString(COL_OVERVIEW);
            OverviewTextView.setText(overview);

            String title = data.getString(COL_TITTLE);
            textTitle.setText(title);

            String date = data.getString(COL_RELEASE_D);
            textDate.setText(date);

            String rate = data.getString(COL_RATING);
            textRate.setText(rate);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}
