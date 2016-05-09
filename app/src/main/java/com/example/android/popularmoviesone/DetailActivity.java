package com.example.android.popularmoviesone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by erikllerena on 4/29/16.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.detail, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        int id = item.getItemId();
//
//          if (id == R.id.sortP) {
////            sortSelect=1;
////            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/popular");
//              startActivity(new Intent(this, SettingsActivity.class));
//              return true;
//        }
//        if (id == R.id.sortR) {
////            sortSelect=2;
////            new FetchMovieList().execute("http://api.themoviedb.org/3/movie/top_rated");
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//
//      //  if (id == R.id.action_settings) {
////            startActivity(new Intent(this, SettingsActivity.class));
////            return true;
//        //}
//        return super.onOptionsItemSelected(item);
//    }

}
