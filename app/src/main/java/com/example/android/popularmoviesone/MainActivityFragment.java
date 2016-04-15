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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

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
public class MainActivityFragment extends Fragment {

    private PosterAdapter thumbPosters;

    private ArrayAdapter<String> mMovietAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMovieList loadMovies = new FetchMovieList();
        loadMovies.execute();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mMovietAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.movie_item,
                        R.id.posterImg,
                        new ArrayList<String>()

                );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movieGrid);
        thumbPosters = new PosterAdapter(getActivity(), Arrays.asList(mMovietAdapter));

        gridView.setVisibility(thumbPosters);


        return rootView;
    }


    public class FetchMovieList extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchMovieList.class.getSimpleName();


        private String[] getMovieDataFromJson(String moviesJsonStr)
                throws JSONException {


            final String PAGES = "results";
            final String POSTER = "poster_path";
            //final String OWM_WEATHER = "weather";
            //final String OWM_TEMPERATURE = "temp";
            //final String OWM_MAX = "max";
            //final String OWM_MIN = "min";
            //final String OWM_DESCRIPTION = "main";

            JSONObject movieGroupJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieGroupJson.getJSONArray(PAGES);

            String[] resultStrs = new String[movieArray.length()];
            for (int i = 0; i < movieArray.length(); i++) {

                String JPGimg;
                JSONObject moviePoster = movieArray.getJSONObject(i);


                //“w185”.

                //JSONObject movieObject = moviePoster.getJSONArray(POSTER).getJSONObject(0);
                JPGimg = moviePoster.getString(POSTER);


                resultStrs[i] = "http://image.tmdb.org/t/p/." + JPGimg;

            }

            return resultStrs;

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

            if (result != null) {
                mMovietAdapter.clear();
                for (String thumbMovie : result) {
                    //Picasso.with(getContext()).load(thumbMovie).into(imageView);
                    mMovietAdapter.add(thumbMovie);
                    //Picasso.with(getContext()).load(thumbMovie).into(imageView);

                }
            }

        }

    }
}


