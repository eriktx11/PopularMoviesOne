package com.example.android.popularmoviesone.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by erikllerena on 4/25/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesone";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String ALL_MOVIE = "t_m_list";
    public static final String ALL_MOVIE_EXTRA = "t_m_extras";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVS = "favs";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";
    public static final String MOVIE_ID = "movie_id";


    public static final class TheMovieList implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(ALL_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ALL_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ALL_MOVIE;

        // Table name
        public static final String TABLE_NAME = "t_m_list";

        public static final String C_RELEASE_D = "release_date";

        public static final String C_RATING = "movie_raiting";
        public static final String C_POPULAR = "popular";
        public static final String C_TOP_RATED = "top_rated";
        public static final String C_TITLE = "movie_title";
        public static final String C_MOVIE_ID = "movie_id";
        public static final String C_OVERVIEW = "overview";
        public static final String C_POSTER_PATH = "poster_path";
        public static final String C_FAV = "fav_movie";


        public static Uri buildMovieUri(String OneId) {
            return CONTENT_URI.buildUpon().appendPath(OneId).build();
        }

        public static Uri buildForPopular(String PopSetting) {
            return CONTENT_URI.buildUpon().appendPath(PopSetting).build();
        }


        public static Uri buildForTopRated(String TopRSetting) {
            return CONTENT_URI.buildUpon().appendPath(TopRSetting).build();
        }

        public static Uri buildForTrailers(String TrailerSetting) {
            return CONTENT_URI.buildUpon().appendPath(TrailerSetting).build();
        }

        public static Uri buildForReviews(String ReviewSetting) {
            return CONTENT_URI.buildUpon().appendPath(ReviewSetting).build();
        }

        public static Uri buildForFavs(String FavSettings) {
            return CONTENT_URI.buildUpon().appendPath(FavSettings).build();
        }

        public static String getOneMovie(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getTopRated(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getFavs(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getOneMovieUri(Uri uri) {
            String dateString = uri.getQueryParameter(C_MOVIE_ID);
            if (null != dateString && dateString.length() > 0)
                return dateString;
            else
                return null;
        }

    }

    public static final class TheMovieExtras implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(ALL_MOVIE_EXTRA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ALL_MOVIE_EXTRA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ALL_MOVIE_EXTRA;

        // Table name
        public static final String TABLE_NAME = "t_m_extras";

        public static final String C_AUTHOR = "review_author";
        public static final String C_CONTENT = "review_content";

        public static final String C_TRAILER_KEY = "trailer_video";

        public static Uri buildFavUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


}
