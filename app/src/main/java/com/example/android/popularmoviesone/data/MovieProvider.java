package com.example.android.popularmoviesone.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by erikllerena on 4/25/16.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataBaseHelper mOpenHelper;


    static final int MOVIE_POPULAR = 100;
    static final int MOVIE_TOP_RATED = 101;
    static final int MOVIE_TRAILERS = 102;
    static final int MOVIE_REVIEWS = 103;
    static final int MOVIE_FAVS = 104;
    static final int MY_MOVIES = 105;
    static final int MOVIE_OVERVIEW = 106;
    static final int MOVIE_TITLE = 107;
    static final int MOVIE_ID = 108;
    static final int ALL = 109;
    static final int TRAILERS_REVIEWS = 200;

    private static final SQLiteQueryBuilder QueryBuilder;

    static{
        QueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON t_m_list.movie_id = t_m_extras._id
        //SELECT * FROM t_m_list inner join t_m_extras on t_m_list.movie_id = t_m_extras._id
        QueryBuilder.setTables(
                MovieContract.TheMovieList.TABLE_NAME + " INNER JOIN " +
                        MovieContract.TheMovieExtras.TABLE_NAME +
                        " ON " + MovieContract.TheMovieList.TABLE_NAME +
                        "." + MovieContract.TheMovieList.C_MOVIE_ID +
                        " = " + MovieContract.TheMovieExtras.TABLE_NAME +
                        "." + MovieContract.TheMovieExtras._ID);
    }



    private static final SQLiteQueryBuilder QueryBuilderP;

    static{
        QueryBuilderP = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON t_m_list.movie_id = t_m_extras._id
        //SELECT * FROM t_m_list inner join t_m_extras on t_m_list.movie_id = t_m_extras._id
        QueryBuilderP.setTables(
                MovieContract.TheMovieList.TABLE_NAME);
    }


    //t_m_list.popular
    private static final String sPopular =
            MovieContract.TheMovieList.TABLE_NAME+
                    "." + MovieContract.TheMovieList.C_POPULAR;


    private static final String sTopRated =
            MovieContract.TheMovieList.TABLE_NAME+
                    "." + MovieContract.TheMovieList.C_TOP_RATED;


    //t_m_list.movie_id = ?
    private static final String oneMovie =
            MovieContract.TheMovieList.TABLE_NAME+
                    "." + MovieContract.TheMovieList.C_MOVIE_ID + " = ";


    private Cursor getPopular(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilderP.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getOneMove(Uri uri, String[] projection, String sortOrder) {

        String MovieIdNeeded = MovieContract.TheMovieList.getOneMovie(uri);
        String[] selectionArgs;
        selectionArgs = new String[]{MovieIdNeeded};

        return QueryBuilderP.query(mOpenHelper.getReadableDatabase(),
                projection,
                oneMovie,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }




    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, MovieContract.ALL_MOVIE, MOVIE_POPULAR);
        matcher.addURI(authority, MovieContract.ALL_MOVIE + "/*", MOVIE_POPULAR);
        matcher.addURI(authority, MovieContract.ALL_MOVIE + "/*/*", MOVIE_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());
        return true;
    }



    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
//content://com.example.android.popularmoviesone/t_m_list/popular
        switch (match) {
            case MOVIE_POPULAR:
                return MovieContract.TheMovieList.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.TheMovieList.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "MOVIE/*"
            case MOVIE_POPULAR:
            {
                retCursor = getPopular(uri, projection, sortOrder);
                break;
            }

            case MOVIE_ID:
            {
//               retCursor = mOpenHelper.getReadableDatabase().query(
//                        MovieContract.TheMovieList.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );

                retCursor = getOneMove(uri, projection, sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri); // linked to update, insert, etc. Important otherwise page is blank
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_POPULAR: {
                long _id = db.insert(MovieContract.TheMovieList.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TheMovieList.buildForPopular("popular");
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ALL: {
                long _id = db.insert(MovieContract.TheMovieList.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TheMovieList.buildForPopular("popular");
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MY_MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.TheMovieList.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS_REVIEWS:
                rowsDeleted = db.delete(
                        MovieContract.TheMovieExtras.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MY_MOVIES:
                rowsUpdated = db.update(MovieContract.TheMovieList.TABLE_NAME, null, selection,
                        selectionArgs);
                break;
            case TRAILERS_REVIEWS:
                rowsUpdated = db.update(MovieContract.TheMovieExtras.TABLE_NAME, null, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_POPULAR:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TheMovieList.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);  // linked to cursor Important otherwise page is blank
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
