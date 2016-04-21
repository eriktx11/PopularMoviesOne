package com.example.android.popularmoviesone;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailPoster extends ActionBarActivity {


    private static final int ANIM_DURATION = 600;
    private TextView OverviewTextView;
    private ImageView imageView;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private FrameLayout frameLayout;
    private ColorDrawable colorDrawable;

    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting details screen layout
        setContentView(R.layout.movie_detail_info);

      //  android.support.v7.app.ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();

        //retrieves the thumbnail data
        Bundle bundle = getIntent().getExtras();
        //thumbnailTop = bundle.getInt("top");
        //thumbnailLeft = bundle.getInt("left");
        //thumbnailWidth = bundle.getInt("width");
        //thumbnailHeight = bundle.getInt("height");


        String overview = bundle.getString("overview");
        String image = bundle.getString("image");

        //initialize and set the image description
        //OverviewTextView = (TextView) findViewById(R.id.movie_text_detail);
        //OverviewTextView.setText(Html.fromHtml(overview));

        //Set image url
        imageView = (ImageView) findViewById(R.id.movie_id_detail);
        Picasso.with(this).load(image).into(imageView);

        }





/*
    private PosterAdapter mGridAdapter;
    private GridView mGridView;
    private ArrayList<GridItem> mGridData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_grid);
        mGridView = (GridView) findViewById(R.id.movieGrid);

        mGridData = new ArrayList<>();
        mGridAdapter = new PosterAdapter(this, R.layout.movie_item, mGridData);
        mGridView.setAdapter(mGridAdapter);



        setContentView(R.layout.movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.detail, menu);
        //return true;

        getMenuInflater().inflate(R.menu.detail, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}

