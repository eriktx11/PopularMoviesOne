package com.example.android.popularmoviesone.interfaces;

import com.example.android.popularmoviesone.BuildConfig;
import com.example.android.popularmoviesone.models.ModelTrailerList;
import com.example.android.popularmoviesone.models.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by erikllerena on 5/26/16.
 */
public interface PosterExtrasAPI {


    @GET("movie/{id}/videos")
    Call<ModelTrailerList> getTrailerList(@Path("id") String movieId, @Query("api_key") String API_KEY);
    //Call<TrailerList> getTrailerList(@Query("id") String movieId, @Query("api_key") String API_KEY);

//    @GET("movie/?/reviews")
//    Call <ReviewList> getReviewList(@Query("id") String movieId, @Query("api_key") String API_KEY);

}

