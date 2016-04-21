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
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.zip.Inflater;

public class MainGrid extends ActionBarActivity {

    private static final String LOG_TAG = MainGrid.class.getSimpleName();

    private PosterAdapter mGridAdapter;
    private GridView mGridView;
    private ArrayList<GridItem> mGridData;
    //private ArrayAdapter<String> mMovieDetailAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {

       setContentView(R.layout.main_grid);
        mGridView = (GridView) findViewById(R.id.movieGrid);

        mGridData = new ArrayList<>();
        mGridAdapter = new PosterAdapter(this, R.layout.movie_item, mGridData);
        mGridView.setAdapter(mGridAdapter);*/

      /*  mMovieDetailAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.movie_detail_info, // The name of the layout ID.
                        R.id.movie_text_detail, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);*/

        // Get a reference to the ListView, and attach this adapter to it.
       /* ListView listView = (ListView) rootView.findViewById(R.id.container);
        listView.setAdapter(mMovieDetailAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mMovieDetailAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailPoster.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });*/

       // return null;
       // }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        mGridData = new ArrayList<>();
        if (id == R.id.sortP) {
            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
        }
        if (id == R.id.sortR) {
            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/top_rated");
        }

        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_grid);
        mGridView = (GridView) findViewById(R.id.movieGrid);

        mGridData = new ArrayList<>();
        mGridAdapter = new PosterAdapter(this, R.layout.movie_item, mGridData);

        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainGrid.this, DetailPoster.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.posterImg);


                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                //intent.putExtra("left", screenLocation[0]).
                //       putExtra("top", screenLocation[1]).
                //       putExtra("width", imageView.getWidth()).
                //       putExtra("height", imageView.getHeight()).
                intent.putExtra("overview", item.getOverview()).
                        putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
    }



        //new DetailPoster();
        //}



        public String result;
        public class FetchMovieList extends AsyncTask<String, Void, String[]> {



            private String[] getMovieDataFromJson(String moviesJsonStr)
                    throws JSONException {

                result = "0";

                final String PAGES = "results";
                final String POSTER = "poster_path";
                final String OVERVIEW = "overview";
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
                    String MovieOverview;
                    JSONObject moviePoster = movieArray.getJSONObject(i);
                    item = new GridItem();
                    JPGimg = moviePoster.getString(POSTER);
                    MovieOverview = moviePoster.getString(OVERVIEW);
                    resultStrs[i] = "http://image.tmdb.org/t/p/w185/" + JPGimg;
                    item.setImage(resultStrs[i]);
                    item.setOverview(MovieOverview);
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

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String moviesJsonStr = null;

                try {

                    final String FORECAST_BASE_URL = params[0];

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


            @Override
            protected void onPostExecute(String[] result) {

                mGridAdapter.setGridData(mGridData);

            }

        }
}




