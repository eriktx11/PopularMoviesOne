package com.example.android.popularmoviesone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainGrid extends ActionBarActivity {

    private static final String LOG_TAG = MainGrid.class.getSimpleName();
    //private PosterAdapter thumbPosters;
    private PosterAdapter mGridAdapter;
    private GridView mGridView;
    private ArrayList<GridItem> mGridData;

    //private ArrayAdapter<String> mMovietAdapter;


    /*@Override
    public void onStart() {
        super.onStart();
        FetchMovieList loadMovies = new FetchMovieList();
        loadMovies.execute();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_grid);
        mGridView = (GridView) findViewById(R.id.movieGrid);

        mGridData = new ArrayList<>();
        mGridAdapter = new PosterAdapter(this, R.layout.movie_item, mGridData);
        mGridView.setAdapter(mGridAdapter);


        new FetchMovieList().execute();


    }


    public String result;
    public class FetchMovieList extends AsyncTask<String, Void, String[]> {
     //   private final String LOG_TAG = FetchMovieList.class.getSimpleName();


        private String[] getMovieDataFromJson(String moviesJsonStr)
                throws JSONException {

            result = "0";

            final String PAGES = "results";
            final String POSTER = "poster_path";
            //final String OWM_WEATHER = "weather";
            //final String OWM_TEMPERATURE = "temp";
            //final String OWM_MAX = "max";
            //final String OWM_MIN = "min";
            //final String OWM_DESCRIPTION = "main";

            JSONObject movieGroupJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieGroupJson.getJSONArray(PAGES);

            GridItem item;
            String[] resultStrs = new String[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {

                String JPGimg;
                JSONObject moviePoster = movieArray.getJSONObject(i);
                item = new GridItem();
                JPGimg = moviePoster.getString(POSTER);
                resultStrs[i] = "http://image.tmdb.org/t/p/." + JPGimg;
                item.setImage(resultStrs[i]);
                mGridData.add(item);

            }

            if (resultStrs!=null){
                result="1";
            }else{
              result="0";}

            return null;
        }


        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String moviesJsonStr = null;


            try {

                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/popular?";
                //final String QUERY_PARAM = "q";
                //final String FORMAT_PARAM = "mode";
                //final String UNITS_PARAM = "units";
                //final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        //.appendQueryParameter(QUERY_PARAM, params[0])
                        //.appendQueryParameter(FORMAT_PARAM, format)
                        //.appendQueryParameter(UNITS_PARAM, units)
                        //.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                moviesJsonStr = buffer.toString();
                // Log.d(LOG_TAG, moviesJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
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

            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }




        //ImageView imageView;

        @Override
        protected void onPostExecute(String[] result) {

            //imageView = new ImageView(getContext());

            //if (result[0] =="1") {
               mGridAdapter.setGridData(mGridData);

              //  }else {
                //Toast.makeText(MainGrid.this, "fail to display", Toast.LENGTH_SHORT).show();
            //}

        }

    }
}


