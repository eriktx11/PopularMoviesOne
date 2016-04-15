package com.example.android.popularmoviesone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by erikllerena on 4/14/16.
 */
public class PosterAdapter extends ArrayAdapter<mMovietAdapter> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();


    public PosterAdapter(Activity context, List<mMovietAdapter> movieTypes) {

        super(context, 0, movieTypes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.posterImg);

        Picasso.with(getContext()).load(mMovietAdapter[position]).into(posterView);


        return convertView;
    }
}



