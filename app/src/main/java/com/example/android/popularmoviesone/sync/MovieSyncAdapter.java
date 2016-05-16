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

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;


        try {


//            private void getMovieDataFromJson(String moviesJsonStr, String trailerJsonStr)
//                    throws JSONException {
//
//                result = "0";
//
//                final String PAGES = "results";
//                final String POSTER = "poster_path";
//                final String OVERVIEW = "overview";
//                final String TITLE = "original_title";
//                final String RATE = "vote_average";
//                final String DATE = "release_date";
//                final String TRAILER_ID = "id";
//                final String KEY = "key";
//
//                JSONObject movieGroupJson = new JSONObject(moviesJsonStr);
//                JSONArray movieArray = movieGroupJson.getJSONArray(PAGES);
//
//                GridItem item;
//                String[] resultStrs = new String[movieArray.length()];
//                for (int i = 0; i < movieArray.length(); i++) {
//
//                    String JPGimg;
//                    String MovieOverview;
//                    String MovieTitle;
//                    String MovieRate;
//                    String MovieDate;
//                    String trailerId;
//
//
//                    JSONObject moviePoster = movieArray.getJSONObject(i);
//                    item = new GridItem();
//                    JPGimg = moviePoster.getString(POSTER);
//                    MovieOverview = moviePoster.getString(OVERVIEW);
//                    MovieTitle = moviePoster.getString(TITLE);
//                    MovieRate = moviePoster.getString(RATE);
//                    MovieDate = moviePoster.getString(DATE);
//                    resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + JPGimg;
//                    trailerId = moviePoster.getString(TRAILER_ID);
//
//                    //trailerJsonStr
//
//
//
//
//                    String Youtube = "https://youtu.be/";
//                    String videoTrailer;
//
//                    JSONObject JsonId = new JSONObject(trailerId);
//                    JSONArray TrailerArray = JsonId.getJSONArray(PAGES);
//
//                    JSONObject movieTrailer = TrailerArray.getJSONObject(0);
//                    videoTrailer = moviePoster.getString(KEY);
//                    Youtube = Youtube + videoTrailer;
//
//
//                    item.setTrailer(Youtube);
//                    item.setImage(resultStrs[i]);
//                    item.setOverview(MovieOverview);
//                    item.setTitle(MovieTitle);
//                    item.setRate(MovieRate);
//                    item.setDate(MovieDate);
//                    mGridData.add(item);
//
//                }



//                final String TRAILER_BASE_URL = trailerUrl;
//
//                final String APPID_PARAM_T = "api_key";
//
//                Uri builtUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
//                        .appendQueryParameter(APPID_PARAM_T, BuildConfig.OPEN_MOVIE_API_KEY)
//                        .build();
//
//                URL url_t = new URL(builtUri.toString());
//
//
//                urlConnectionT = (HttpURLConnection) url_t.openConnection();
//                urlConnectionT.setRequestMethod("GET");
//                urlConnectionT.connect();
//
//                InputStream inputStreamT = urlConnectionT.getInputStream();
//                StringBuffer bufferT = new StringBuffer();
//                String lineT;


            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/popular";
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
            getMovieDataFromJson(movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
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


//    HttpURLConnection urlConnectionT = null;
//    BufferedReader readerT = null;
//
//    // Will contain the raw JSON response as a string.
//    String movieJsonStrT = null;
//
//    HttpURLConnection urlConnectionR = null;
//    BufferedReader readerR = null;
//
//    // Will contain the raw JSON response as a string.
//    String movieJsonStrR = null;
//
//
//    try {
//
//
////            private void getMovieDataFromJson(String moviesJsonStr, String trailerJsonStr)
////                    throws JSONException {
////
////                result = "0";
////
////                final String PAGES = "results";
////                final String POSTER = "poster_path";
////                final String OVERVIEW = "overview";
////                final String TITLE = "original_title";
////                final String RATE = "vote_average";
////                final String DATE = "release_date";
////                final String TRAILER_ID = "id";
////                final String KEY = "key";
////
////                JSONObject movieGroupJson = new JSONObject(moviesJsonStr);
////                JSONArray movieArray = movieGroupJson.getJSONArray(PAGES);
////
////                GridItem item;
////                String[] resultStrs = new String[movieArray.length()];
////                for (int i = 0; i < movieArray.length(); i++) {
////
////                    String JPGimg;
////                    String MovieOverview;
////                    String MovieTitle;
////                    String MovieRate;
////                    String MovieDate;
////                    String trailerId;
////
////
////                    JSONObject moviePoster = movieArray.getJSONObject(i);
////                    item = new GridItem();
////                    JPGimg = moviePoster.getString(POSTER);
////                    MovieOverview = moviePoster.getString(OVERVIEW);
////                    MovieTitle = moviePoster.getString(TITLE);
////                    MovieRate = moviePoster.getString(RATE);
////                    MovieDate = moviePoster.getString(DATE);
////                    resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + JPGimg;
////                    trailerId = moviePoster.getString(TRAILER_ID);
////
////                    //trailerJsonStr
////
////
////
////
////                    String Youtube = "https://youtu.be/";
////                    String videoTrailer;
////
////                    JSONObject JsonId = new JSONObject(trailerId);
////                    JSONArray TrailerArray = JsonId.getJSONArray(PAGES);
////
////                    JSONObject movieTrailer = TrailerArray.getJSONObject(0);
////                    videoTrailer = moviePoster.getString(KEY);
////                    Youtube = Youtube + videoTrailer;
////
////
////                    item.setTrailer(Youtube);
////                    item.setImage(resultStrs[i]);
////                    item.setOverview(MovieOverview);
////                    item.setTitle(MovieTitle);
////                    item.setRate(MovieRate);
////                    item.setDate(MovieDate);
////                    mGridData.add(item);
////
////                }
//
//
////                final String TRAILER_BASE_URL = trailerUrl;
////
////                final String APPID_PARAM_T = "api_key";
////
////                Uri builtUri = Uri.parse(TRAILER_BASE_URL).buildUpon()
////                        .appendQueryParameter(APPID_PARAM_T, BuildConfig.OPEN_MOVIE_API_KEY)
////                        .build();
////
////                URL url_t = new URL(builtUri.toString());
////
////
////                urlConnectionT = (HttpURLConnection) url_t.openConnection();
////                urlConnectionT.setRequestMethod("GET");
////                urlConnectionT.connect();
////
////                InputStream inputStreamT = urlConnectionT.getInputStream();
////                StringBuffer bufferT = new StringBuffer();
////                String lineT;
//
//
//        final String T_BASE_URL =
//                "http://api.themoviedb.org/3/movie/" + id + "/videos";
//        final String R_BASE_URL =
//                "http://api.themoviedb.org/3/movie/" + id + "/reviews";
//
//        final String APPID_PARAM = "api_key";
//        Uri builtUriT = Uri.parse(T_BASE_URL).buildUpon()
//                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
//                .build();
//
//        Uri builtUriR = Uri.parse(R_BASE_URL).buildUpon()
//                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
//                .build();
//
//        URL urlT = new URL(builtUriT.toString());
//        URL urlR = new URL(builtUriR.toString());
//
//        urlConnectionT = (HttpURLConnection) urlT.openConnection();
//        urlConnectionT.setRequestMethod("GET");
//        urlConnectionT.connect();
//
//        urlConnectionR = (HttpURLConnection) urlR.openConnection();
//        urlConnectionR.setRequestMethod("GET");
//        urlConnectionR.connect();
//
//        InputStream inputStreamT = urlConnectionT.getInputStream();
//        StringBuffer bufferT = new StringBuffer();
//
//        InputStream inputStreamR = urlConnectionR.getInputStream();
//        StringBuffer bufferR = new StringBuffer();
//
//        if (inputStreamT == null || inputStreamR == null) {
//            // Nothing to do.
//            return true;
//        }
//        readerT = new BufferedReader(new InputStreamReader(inputStreamT));
//        readerR = new BufferedReader(new InputStreamReader(inputStreamR));
//
//        String lineT;
//        String lineR;
//        while ((lineT = readerT.readLine()) != null) {
//            bufferT.append(lineT + "\n");
//        }
//
//        while ((lineR = readerR.readLine()) != null) {
//            bufferR.append(lineR + "\n");
//        }
//
//        if (bufferT.length() == 0) {
//            return true;
//        }
//
//        if (bufferR.length() == 0) {
//            return true;
//        }
//
//        movieJsonStrT = bufferT.toString();
//        movieJsonStrR = bufferR.toString();
//
//
//    } catch (IOException e) {
//        Log.e(LOG_TAG, "Error ", e);
//        // If the code didn't successfully get the weather data, there's no point in attempting
//        // to parse it.
//    } catch (JSONException e) {
//        Log.e(LOG_TAG, e.getMessage(), e);
//        e.printStackTrace();
//    } finally {
//        if (urlConnectionT != null || urlConnectionR != null) {
//            urlConnectionT.disconnect();
//            urlConnectionR.disconnect();
//        }
//        if (readerT != null || readerR != null) {
//            try {
//                readerT.close();
//            } catch (final IOException e) {
//                Log.e(LOG_TAG, "Error closing stream", e);
//            }
//        }
//    }
//    return true;
//
//}

    private void getMovieDataFromJson(String forecastJsonStr)
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


                // Get the JSON object representing the day
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
             //   movieParts.put(MovieContract.TheMovieList.C_TOP_RATED, rate);
                movieParts.put(MovieContract.TheMovieList.C_POSTER_PATH, poster);
             //   movieParts.put(MovieContract.TheMovieList.C_FAV, "0");
             //   movieParts.put(MovieContract.TheMovieList.C_POPULAR, "1");
              //  movieParts.put(MovieContract.TheMovieList.C_TOP_RATED, "0");
              //  movieParts.put(MovieContract.TheMovieList.C_OVERVIEW, overview);

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


    public static void syncImmediately(Context context) {
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

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

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
        syncImmediately(context);
    }


    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
