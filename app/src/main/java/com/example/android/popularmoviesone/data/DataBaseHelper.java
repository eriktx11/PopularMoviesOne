package com.example.android.popularmoviesone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesone.data.MovieContract.TheMovieList;
import com.example.android.popularmoviesone.data.MovieContract.TheMovieExtras;

/**
 * Created by erikllerena on 4/25/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    public static String DB_NAME = "mtheater.db";

    public static final int DB_VERSION = 3;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_MOVIE_LIST_TABLE = "CREATE TABLE " + MovieContract.TheMovieList.TABLE_NAME + " (" +
                TheMovieList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TheMovieList.C_OVERVIEW + " TEXT NOT NULL, " +
                TheMovieList.C_RATING + " TEXT NOT NULL, " +
                TheMovieList.C_RELEASE_D + " TEXT NOT NULL, " +
                TheMovieList.C_MOVIE_ID + " TEXT NOT NULL, " +
                TheMovieList.C_TITLE + " TEXT NOT NULL, " +
                TheMovieList.C_POPULAR + " TEXT NOT NULL, " +
                TheMovieList.C_TOP_RATED + " TEXT NOT NULL, " +
                TheMovieList.C_FAV + " TEXT NOT NULL, " +
                TheMovieList.C_POSTER_PATH + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + TheMovieList.C_MOVIE_ID + ") REFERENCES " +
                TheMovieExtras.TABLE_NAME + " (" + TheMovieExtras._ID + ") " +
                " );";

//        public static final String TABLE_NAME = "t_m_list";
//
//        public static final String C_RELEASE_D = "release_date";
//
//        public static final String C_RATING = "movie_raiting";
//
//        public static final String C_TITLE = "movie_title";
//        public static final String C_MOVIE_ID = "movie_id";
//        public static final String C_OVERVIEW = "overview";
//        public static final String C_POSTER_PATH = "poster_path";
//        public static final String C_FAV = "fav_movie";

        db.execSQL(SQL_CREATE_MOVIE_LIST_TABLE);


        final String SQL_CREATE_MOVIE_EXTRAS_TABLE = "CREATE TABLE " + MovieContract.TheMovieExtras.TABLE_NAME + " (" +
                TheMovieExtras._ID + " TEXT NOT NULL," +
                TheMovieExtras.C_AUTHOR + " TEXT NOT NULL, " +
                TheMovieExtras.C_CONTENT + " TEXT NOT NULL, " +
                TheMovieExtras.C_TRAILER_KEY + " TEXT NOT NULL " +
                " );";




//        CREATE TABLE one  (
//                ID  TEXT NOT NULL,
//                C_AUTHOR  TEXT NOT NULL,
//                C_CONTENT  TEXT NOT NULL,
//                C_TRAILER_KEY TEXT NOT NULL )

        db.execSQL(SQL_CREATE_MOVIE_EXTRAS_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TheMovieList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TheMovieExtras.TABLE_NAME);
        onCreate(db);

    }


}