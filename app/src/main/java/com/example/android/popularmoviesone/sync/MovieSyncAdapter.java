package com.example.android.popularmoviesone.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import com.example.android.popularmoviesone.BuildConfig;
import com.example.android.popularmoviesone.Utility;
import com.example.android.popularmoviesone.R;
import com.example.android.popularmoviesone.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by erikllerena on 4/29/16.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        String popularQuery = Utility.getPreferredLocation(getContext());
        int select = extras.getInt("select");
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;


        try {

            String MOVIE_BASE_URL="";
            switch (select) {
                case 1: MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular";
                    break;
                case 2: MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/top_rated";
                    break;
            }

            final String APPID_PARAM = "api_key";
            //final String POPULAR_MOVIE = "popular";
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    //.appendQueryParameter(POPULAR_MOVIE, popularQuery)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr, select);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return;
    }



    private void getMovieDataFromJson(String forecastJsonStr, int select)
            throws JSONException {

                final String PAGES = "results";
                final String POSTER = "poster_path";
                final String OVERVIEW = "overview";
                final String TITLE = "original_title";
                final String RATE = "vote_average";
                final String DATE = "release_date";
                final String MOVIE_ID = "id";
                final String KEY = "key";
                final String CONTENT = "content";
                final String AUTHOR = "author";


        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movierArray = forecastJson.getJSONArray(PAGES);


            // Insert the new movie information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movierArray.length());

            for(int i = 0; i < movierArray.length(); i++) {

                String poster;
                String overview;
                String title;
                String rate;
                String date;
                String id;
                String key;
                String content;
                String author;

                JSONObject ScanEachMovie = movierArray.getJSONObject(i);

                poster = ScanEachMovie.getString(POSTER);
                overview = ScanEachMovie.getString(OVERVIEW);
                title = ScanEachMovie.getString(TITLE);
                rate = ScanEachMovie.getString(RATE);
                date = ScanEachMovie.getString(DATE);
                id = ScanEachMovie.getString(MOVIE_ID);

                ContentValues movieParts = new ContentValues();

                movieParts.put(MovieContract.TheMovieList.C_MOVIE_ID, id);
                movieParts.put(MovieContract.TheMovieList.C_TITLE, title);
                movieParts.put(MovieContract.TheMovieList.C_RELEASE_D, date);
                if (select==2){
                    movieParts.put(MovieContract.TheMovieList.C_TOP_RATED, "1");
                }else {
                    movieParts.put(MovieContract.TheMovieList.C_TOP_RATED, "0");
                }
                movieParts.put(MovieContract.TheMovieList.C_POSTER_PATH, poster);


                movieParts.put(MovieContract.TheMovieList.C_FAV, "0");

                if (select==1){
                    movieParts.put(MovieContract.TheMovieList.C_POPULAR, "1");
                }else {
                    movieParts.put(MovieContract.TheMovieList.C_POPULAR, "0");
                }

                movieParts.put(MovieContract.TheMovieList.C_RATING, rate);
                movieParts.put(MovieContract.TheMovieList.C_OVERVIEW, overview);

                cVVector.add(movieParts);
            }




            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MovieContract.TheMovieList.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
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
        bundle.putInt("select", select);
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
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

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
}
