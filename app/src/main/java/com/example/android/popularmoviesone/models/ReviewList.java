package com.example.android.popularmoviesone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by erikllerena on 6/2/16.
 */
public class ReviewList {

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("content")
    @Expose
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
