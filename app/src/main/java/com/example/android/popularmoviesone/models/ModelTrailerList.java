package com.example.android.popularmoviesone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erikllerena on 5/26/16.
 */
public class ModelTrailerList{


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<TrailerList> results;

    public void setId(int id) {
        this.id = id;
    }

    public List<TrailerList> getResults() {
        return results;
    }

}
