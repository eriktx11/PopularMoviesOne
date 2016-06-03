package com.example.android.popularmoviesone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by erikllerena on 6/2/16.
 */
public class ModelReviewList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("results")
    @Expose
    private List<ReviewList> results;

    public String getId(String id) {
        return id;
    }

    public List<ReviewList> getRevResults() {
        return results;
    }

}
