package com.example.android.popularmoviesone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.android.popularmoviesone.data.MovieContract;
import com.example.android.popularmoviesone.interfaces.PosterExtrasAPI;
import com.example.android.popularmoviesone.models.ModelTrailerList;
import com.example.android.popularmoviesone.models.TrailerList;
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
import java.util.Iterator;
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
            MovieContract.TheMovieExtras.TABLE_NAME + "." + MovieContract.TheMovieExtras._ID,
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

    public static final int COL_EXTRA_MOVIE_ID = 10;
    public static final int COL_CONTENT = 11;
    public static final int COL_AUTHOR = 12;
    public static final int COL_TRAILER_KEY = 13;

    private TextView OverviewTextView;
    private TextView textTitle;
    private TextView textRate;
    private TextView textDate;
    private ImageView imageView;
    private VideoView VideoTrailer;

    private ImageButton playTrailer;
    private PosterExtrasAPI posterExtraAPI;

    private static final int FORECAST_LOADER = 1;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    private SharedPreferences sPrefs;
    SharedPreferences.Editor editor;
    String fromList="";

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sPrefs.edit();
        editor.putString("theLink", fromList);
        editor.apply();

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

            Call<ModelTrailerList> MTrailerListCall = posterExtraAPI.getTrailerList(extractedMovieId, BuildConfig.OPEN_MOVIE_API_KEY);

            MTrailerListCall.enqueue(new Callback<ModelTrailerList>() {
                @Override
                public void onResponse(Call<ModelTrailerList> call, Response<ModelTrailerList> response) {
                    int statusCode = response.code();
                    ModelTrailerList trailerList = response.body();
                    Log.d("TrailerList", "onResponse: " + statusCode);

                    List <TrailerList> list = trailerList.getResults();
                    String key="";
                    String content="TBD";
                    String author="me";

                    int count=0;
                    for (TrailerList element : list) {

                        key = element.getKey();
                        count++;

                    }

                    ContentValues movieParts = new ContentValues();

                    Vector<ContentValues> cVVector = new Vector<ContentValues>(1);
                    movieParts.put(MovieContract.TheMovieExtras._ID, extractedMovieId);
                    movieParts.put(MovieContract.TheMovieExtras.C_TRAILER_KEY, key);
                    movieParts.put(MovieContract.TheMovieExtras.C_AUTHOR, author);
                    movieParts.put(MovieContract.TheMovieExtras.C_CONTENT, content);

                    cVVector.add(movieParts);
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    getContext().getContentResolver().bulkInsert(MovieContract.TheMovieExtras.CONTENT_URI, cvArray);

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
        playTrailer = (ImageButton) rootView.findViewById(R.id.playbtn);

        ImageView iv = (ImageView) rootView.findViewById(R.id.favbtn);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.fav_off);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 40, 40, true);
        iv.setImageBitmap(bMapScaled);

        ImageView playImg = (ImageView) rootView.findViewById(R.id.playbtn);
        Bitmap bMapPlay = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        Bitmap bMapPlayScaled = Bitmap.createScaledBitmap(bMapPlay, 60, 60, true);
        playImg.setImageBitmap(bMapPlayScaled);

        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        Context context = getContext();

        syncImmediately(context, 1);

        return rootView;
    }


    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context, int select) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        DetailFragment.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context, 1);
    }


    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
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
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        if (data != null && data.moveToFirst()) {


            String poster = data.getString(COL_POSTER_PATH);

            poster = "http://image.tmdb.org/t/p/w185/"+poster;

            Picasso.with(getContext()).load(poster).into(imageView);


            fromList = data.getString(COL_TRAILER_KEY);

            fromList = "https://youtu.be/"+fromList;

            editor.putString("theLink", fromList);
            editor.apply();

            playTrailer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v)  {

                    String internalData = sPrefs.getString("theLink", fromList);
                    Intent toYouT = new Intent(Intent.ACTION_VIEW, Uri.parse(internalData));
                    startActivity(toYouT);

                }
            });



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
