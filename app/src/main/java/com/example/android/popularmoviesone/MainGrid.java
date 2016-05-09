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


import com.example.android.popularmoviesone.data.MovieContract;
import com.example.android.popularmoviesone.sync.MovieSyncAdapter;

public class MainGrid extends ActionBarActivity implements ForecastFragment.Callback {

    private final String LOG_TAG = MainGrid.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_grid);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment forecastFragment =  ((ForecastFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);

        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


//
          if (id == R.id.sortP) {
//            sortSelect=1;
//            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
              startActivity(new Intent(this, SettingsActivity.class));
              return true;
        }
        if (id == R.id.sortR) {
//            sortSelect=2;
//            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/top_rated");
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }




//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//        if (id == R.id.action_map) {
//            openPreferredLocationInMap();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // update the location in our second pane using the fragment manager
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}




















//public class MainGrid extends ActionBarActivity {
//
//    private static final String LOG_TAG = MainGrid.class.getSimpleName();
//
//    private PosterAdapter mGridAdapter;
//    private GridView mGridView;
//    private ArrayList<GridItem> mGridData;
//
//
//    static final String STATE_SORTING="UserSelection";
//    public int sortSelect=1;
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//
//        savedInstanceState.putInt(STATE_SORTING, sortSelect);
//
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        sortSelect = savedInstanceState.getInt(STATE_SORTING);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        Stetho.initializeWithDefaults(this);
//        return true;
//    }
//
//
//    DataBaseHelper myDB;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        myDB = new DataBaseHelper(this);
//
//        setContentView(R.layout.main_grid);
//        mGridView = (GridView) findViewById(R.id.movieGrid);
//
//        mGridData = new ArrayList<>();
//        mGridAdapter = new PosterAdapter(this, R.layout.movie_item, mGridData);
//
//        mGridView.setAdapter(mGridAdapter);
//
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                GridItem item = mGridData.get(position);
//                Intent intent = new Intent(MainGrid.this, DetailPoster.class);
//                ImageView imageView = (ImageView) v.findViewById(R.id.posterImg);
//
//
//                int[] screenLocation = new int[2];
//                imageView.getLocationOnScreen(screenLocation);
//
//                intent.putExtra("overview", item.getOverview()).
//                        putExtra("image", item.getImage()).
//                        putExtra("title", item.getTitle()).
//                        putExtra("rate", item.getRate()).
//                        putExtra("date", item.getDate());
//
//
//                startActivity(intent);
//            }
//        });
//
//
//
//        if (savedInstanceState==null){
//            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
//        }else {
//            if(savedInstanceState.getInt(STATE_SORTING)==sortSelect){
//                new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
//            }else {
//                new FetchMovieList().execute("http://api.themoviedb.org/3/movie/top_rated");
//            }
//
//        }
//    }
//
//
//
//
//        public String result;
//        public class FetchMovieList extends AsyncTask<String, Void, Void> {
//
//
//
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
//
//                if (resultStrs!=null){
//                    result="1";
//                }else{
//                    result="0";}
//
//            }
//
//
//            @Override
//            protected Void doInBackground(String... params) {
//
//                HttpURLConnection urlConnection = null;
//                BufferedReader reader = null;
//
//                String moviesJsonStr = null;
//
//
//                try {
//
//                    final String FORECAST_BASE_URL = params[0];
//
//                    final String APPID_PARAM = "api_key";
//
//                    Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//
//                            .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
//                            .build();
//
//                    URL url = new URL(builtUri.toString());
//
//
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//
//
//                    InputStream inputStream = urlConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//                    if (inputStream == null) {
//
//                        return null;
//                    }
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//
//                        buffer.append(line + "\n");
//                    }
//
//                    if (buffer.length() == 0) {
//
//                        return null;
//                    }
//                    moviesJsonStr = buffer.toString();
//
//                } catch (IOException e) {
//                    Log.e(LOG_TAG, "Error ", e);
//
//                    return null;
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (final IOException e) {
//                            Log.e(LOG_TAG, "Error closing stream", e);
//                        }
//                    }
//                }
//
//                try {
//                    return getMovieDataFromJson(moviesJsonStr);
//                } catch (JSONException e) {
//                    Log.e(LOG_TAG, e.getMessage(), e);
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//
////            @Override
////            protected void onPostExecute(String[] result) {
////
////                mGridAdapter.setGridData(mGridData);
////
////            }
//
//
//            //start pulling trailer
//
//            HttpURLConnection urlConnectionT = null;
//            BufferedReader readerT = null;
//            String trailerUrl;
//
//            try {
//
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
//                if (inputStreamT == null) {
//
//                    return null;
//                }
//                readerT = new BufferedReader(new InputStreamReader(inputStreamT));
//
//
//                while ((lineT = readerT.readLine()) != null) {
//
//                    bufferT.append(lineT + "\n");
//                }
//
//                if (bufferT.length() == 0) {
//
//                    return null;
//                }
//                trailerId = bufferT.toString();
//
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//
//            } finally {
//                if (urlConnectionT != null) {
//                    urlConnectionT.disconnect();
//                }
//                if (readerT != null) {
//                    try {
//                        readerT.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//
//            //end pulling for trailer
//
//
//        }
//}




