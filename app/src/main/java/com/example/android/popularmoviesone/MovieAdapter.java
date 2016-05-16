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

//    /**
//     * Cache of the children views for a forecast list item.
//     */


//    private Context mContex;
//    private int layoutResourceId;
//    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();



    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


//        public static class ViewHolder(View view) {
//
//            imageView = (ImageView) view.findViewById(R.id.posterImg);
//
//            View row = convertView;
//            ViewHolder holder;
//
//            if (row==null){
//                LayoutInflater inflater = ((Activity) mContex).getLayoutInflater();
//                row = inflater.inflate(layoutResourceId, parent, false);
//                holder = new ViewHolder();
//                imageView = (ImageView) row.findViewById(R.id.posterImg);
//                row.setTag(holder);
//            }
//            else {
//                holder = (ViewHolder) row.getTag();
//            }
//
//            GridItem item = mGridData.get(position);
//
//            Picasso.with(mContex).load(item.getImage()).into(imageView);
//
//            return row;
//        }
//    }


    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    String[] contents;



    private Context mContex;
    private Cursor c;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();


//    public MovieAdapter (Context mContex, int layoutResourceId, ArrayList<GridItem> mGridData){
//        super(mContex, layoutResourceId, mGridData);
//
//        this.mContex=mContex;
//        this.layoutResourceId=layoutResourceId;
//        this.mGridData=mGridData;
//
//    }


    public MovieAdapter (Context mContex, Cursor c, int flags){
        super(mContex, c, flags);

        this.mContex=mContex;
        this.layoutResourceId=layoutResourceId;
        this.mGridData=mGridData;
        this.c=c;

    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//
//
//
//    }


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

/// /    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent){
//        Log.v(LOG_TAG, "new view called");
//        View newView = LayoutInflater.from(context).inflate(R.layout.movie_detail, parent, false);
//        return newView;
//    }

    public static class ViewHolder{
        public final ImageView imageView;

        public ViewHolder (View view){

            imageView = (ImageView) view.findViewById(R.id.posterImg);
        }
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        //ImageView imageView;
//        View row = convertView;
//        //ViewHolder holder;
//
//        //Context context;
//        //Cursor cursor;
//
//        //context = getContext();
//        //cursor = null;
//
//        DatabaseUtils.dumpCursor(c);
//        Log.v(LOG_TAG, "bind view called" + c.getCount());
//        String[] bind = convertCursorRowToUXFormat(c);
//        Log.v(LOG_TAG, "cursor contents" + bind[0] + bind[1] + bind[2] + bind[3]);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.posterImg);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        if(imageView!=null){
//            Log.v(LOG_TAG, "imageview in bindview called");
//            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + bind[5]);
//            Picasso.with(mContext).load(uri).into(imageView);
//            imageView.setAdjustViewBounds(true);
//        }
//
//
////        if (row==null){
////            LayoutInflater inflater = ((Activity) mContex).getLayoutInflater();
////            row = inflater.inflate(layoutResourceId, parent, false);
////            holder = new ViewHolder();
////            holder.imageView = (ImageView) row.findViewById(R.id.posterImg);
////            row.setTag(holder);
////        }
////        else {
////            holder = (ViewHolder) row.getTag();
////        }
////
////        GridItem item = mGridData.get(position);
////
////        Picasso.with(mContex).load(item.getImage()).into(holder.imageView);
//
//        return row;
//
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.movie_detail_info;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.movie_detail_info;
                break;
            }
        }

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


    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}

//
//
//    private Context mContex;
//    private int layoutResourceId;
//    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

//    public MovieAdapter (Context mContex, Cursor c, int flags){
//        super(mContex, c, flags);
//
//        this.mContex=mContex;
//        this.layoutResourceId=layoutResourceId;
//        this.mGridData=mGridData;
//
//    }
//
//    public void setGridData(ArrayList<GridItem> mGridData) {
//        this.mGridData = mGridData;
//        notifyDataSetChanged();
//    }
//
////    @Override
////    public View getView(int position, View convertView, ViewGroup parent){
////
////        View row = convertView;
////        ViewHolder holder;
////
////        if (row==null){
////            LayoutInflater inflater = ((Activity) mContex).getLayoutInflater();
////            row = inflater.inflate(layoutResourceId, parent, false);
////            holder = new ViewHolder();
////            holder.imageView = (ImageView) row.findViewById(R.id.posterImg);
////            row.setTag(holder);
////        }
////        else {
////            holder = (ViewHolder) row.getTag();
////        }
////
////        GridItem item = mGridData.get(position);
////
////        Picasso.with(mContex).load(item.getImage()).into(holder.imageView);
////
////        return row;
////
////    }
//
//
//
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent, View convertView) {
//        // Choose the layout type
////        int viewType = getItemViewType(cursor.getPosition());
////        int layoutId = -1;
////        switch (viewType) {
////            case VIEW_TYPE_TODAY: {
////                layoutId = R.layout.movie_item;
////                break;
////            }
////            case VIEW_TYPE_FUTURE_DAY: {
////                layoutId = R.layout.movie_item;
////                break;
////            }
////        }
////
////        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
////
////        ViewHolder viewHolder = new ViewHolder(view);
////        view.setTag(viewHolder);
//
//        //return view;
//
//
//        View row = convertView;
//        ViewHolder holder;
//
//        if (row==null){
//            LayoutInflater inflater = ((Activity) mContex).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//            holder = new ViewHolder(row);
//            //holder.imageView = (ImageView) row.findViewById(R.id.posterImg);
//            row.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) row.getTag();
//        }
//
//        GridItem item = mGridData.get();
//
//        Picasso.with(mContex).load(item.getImage()).into(holder.imageView);
//
//        return row;
//
//
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
////
//        ViewHolder viewHolder = (ViewHolder) view.getTag();
////
//        int viewType = getItemViewType(cursor.getPosition());
////        switch (viewType) {
////            case VIEW_TYPE_TODAY: {
////                // Get weather icon
////                viewHolder.imageView.setImageResource(Utility.getArtResourceForWeatherCondition(
////                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
////                break;
////            }
////            case VIEW_TYPE_FUTURE_DAY: {
////                // Get weather icon
////                viewHolder.imageView.setImageResource(Utility.getIconResourceForWeatherCondition(
////                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
////                break;
////            }
////        }
//
//        // Read date from cursor
//        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
//        // Find TextView and set formatted date on it
//        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));
//
//        GridItem item = mGridData.get(position);
//
//        Picasso.with(mContex).load(item.getImage()).into(ViewHolder);
//
//        // Read weather forecast from cursor
//        String description = cursor.getString(ForecastFragment.COL_POSTER_PATH);
//        // Find TextView and set weather forecast on it
//        viewHolder.descriptionView.setText(description);
//
//        // For accessibility, add a content description to the icon field
//        viewHolder.iconView.setContentDescription(description);
//
//        // Read user preference for metric or imperial temperature units
//        boolean isMetric = Utility.isMetric(context);
//
//        // Read high temperature from cursor
//        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
//        viewHolder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));
//
//        // Read low temperature from cursor
//        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
//        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
//    }
//
//    public void setUseTodayLayout(boolean useTodayLayout) {
//        mUseTodayLayout = useTodayLayout;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return VIEW_TYPE_COUNT;
//    }
//}
