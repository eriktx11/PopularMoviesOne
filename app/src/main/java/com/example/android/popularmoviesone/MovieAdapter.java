package com.example.android.popularmoviesone;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.popularmoviesone.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by erikllerena on 5/6/16.
 */
public class MovieAdapter extends CursorAdapter{
        //ArrayAdapter<GridItem> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;


    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;



    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    String[] contents;


    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();


    public MovieAdapter (Context contex, Cursor c, int flags){


        super(contex, c, flags);
    }


    private String[] convertCursorRowToUXFormat(Cursor cursor){

        contents = new String[10];
        contents[0] = cursor.getString(ForecastFragment.COL_ID);
        contents[1] = cursor.getString(ForecastFragment.COL_TITLE);
        contents[2] = cursor.getString(ForecastFragment.COL_OVERVIEW);
        contents[3] = cursor.getString(ForecastFragment.COL_RATING);
        contents[4] = cursor.getString(ForecastFragment.COL_RELEASE_D);
        contents[5] = cursor.getString(ForecastFragment.COL_POSTER_PATH);
        contents[6] = cursor.getString(ForecastFragment.COL_FAV);
        contents[7] = cursor.getString(ForecastFragment.COL_POPULAR);
        contents[8] = cursor.getString(ForecastFragment.COL_TOP_RATED);
        contents[9] = cursor.getString(ForecastFragment.COL_MOVIE_ID);

        return contents;
    }


    public static class ViewHolder{
        public final ImageView imageView;

        public ViewHolder (View view){

            imageView = (ImageView) view.findViewById(R.id.posterImg);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
      //  int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
      //  switch (viewType) {
       //     case VIEW_TYPE_TODAY: {
//                layoutId = R.layout.movie_item;//movie_detail_info
//                break;
//            }
//            case VIEW_TYPE_FUTURE_DAY: {
//                layoutId = R.layout.movie_item;
//                break;
//            }
//        }
                layoutId = R.layout.movie_item;//movie_detail_info
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
        Log.v(LOG_TAG, "bind view called" + cursor.getCount());
        String[] bind = convertCursorRowToUXFormat(cursor);
        Log.v(LOG_TAG, "cursor contents" + bind[0] + bind[1] + bind[2] + bind[3]);
        ImageView imageView = (ImageView) view.findViewById(R.id.posterImg);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(imageView!=null){
            Log.v(LOG_TAG, "imageview in bindview called");
            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + bind[5]);
            Picasso.with(context).load(uri).into(imageView);
            imageView.setAdjustViewBounds(true);
        }
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }


//    @Override
//    public int getItemViewType(int position) {
//        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return VIEW_TYPE_COUNT;
//    }//VIEW_TYPE_COUNT
}
