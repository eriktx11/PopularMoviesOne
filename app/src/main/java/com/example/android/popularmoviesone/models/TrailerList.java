package com.example.android.popularmoviesone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by erikllerena on 5/26/16.
 */
public class TrailerList{

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("key")
    @Expose
    private String key;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
