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
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
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


    private TextView OverviewTextView;
    private TextView textTitle;
    private TextView textRate;
    private TextView textDate;
    private ImageView imageView;

    //private int mLeftDelta;
    //private int mTopDelta;
    //private float mWidthScale;
    //private float mHeightScale;

    private FrameLayout frameLayout;
    private ColorDrawable colorDrawable;

    //private int thumbnailTop;
    //private int thumbnailLeft;
    //private int thumbnailWidth;
    //private int thumbnailHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail_info);

        Bundle bundle = getIntent().getExtras();
        //thumbnailTop = bundle.getInt("top");
        //thumbnailLeft = bundle.getInt("left");
        //thumbnailWidth = bundle.getInt("width");
        //thumbnailHeight = bundle.getInt("height");


        String overview = bundle.getString("overview");
        String image = bundle.getString("image");
        String title = bundle.getString("title");
        String rate = bundle.getString("rate");
        String date = bundle.getString("date");

        //initialize and set the image description
        OverviewTextView = (TextView) findViewById(R.id.movie_text_detail);
        OverviewTextView.setText(Html.fromHtml(overview));
        OverviewTextView.setMovementMethod(new ScrollingMovementMethod());

        textTitle = (TextView) findViewById(R.id.MovieTitle);
        textTitle.setText(Html.fromHtml(title));

        textRate = (TextView) findViewById(R.id.MovieRating);
        textRate.setText(Html.fromHtml("Rating: "+rate+"/10"));

        textDate = (TextView) findViewById(R.id.MovieReleaseDate);
        textDate.setText(Html.fromHtml("Release date: "+date));

        //Set image url
        imageView = (ImageView) findViewById(R.id.movie_id_detail);
        Picasso.with(this).load(image).into(imageView);

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

