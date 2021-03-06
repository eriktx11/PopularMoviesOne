package com.example.android.popularmoviesone;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * Created by erikllerena on 4/25/16.
 */
public class TestMovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DataBaseHelperTest mOpenHelper;


    static final int MOVIE_POPULAR = 100;
    static final int MOVIE_TOP_RATED = 101;
    static final int MOVIE_TRAILERS = 102;
    static final int MOVIE_REVIEWS = 103;
    static final int MOVIE_FAVS = 104;
    static final int MY_MOVIES = 105;
    static final int MOVIE_OVERVIEW = 106;
    static final int MOVIE_TITLE = 107;
    static final int MOVIE_ID = 108;
    static final int TRAILERS_REVIEWS = 200;

    private static final SQLiteQueryBuilder QueryBuilder;

    static{
        QueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON t_m_list.movie_id = t_m_extras._id
        QueryBuilder.setTables(
                com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME + " INNER JOIN " +
                        com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME +
                        " ON " + com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME +
                        "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieList.C_MOVIE_ID +
                        " = " + com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME +
                        "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras._ID);
    }


    private static final String sPopular =
            com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME+
                    "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieList.C_POPULAR + "popular";


    private static final String sTopRated =
            com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME+
                    "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieList.C_TOP_RATED + "top_rated";


    private static final String sTrailers =
            com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME+
                    "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.C_TRAILER_KEY + "trailers";


    private static final String sReviews =
            com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME+
                    "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.C_CONTENT + "reviews";


    private static final String sFavs =
            com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME+
                    "." + com.example.android.popularmoviesone.TestMovieContract.TheMovieList.C_FAV + "favs";


    private Cursor getPopular(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sPopular,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTopRated(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTopRated,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailers(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTrailers,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviews(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sReviews,
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavs(Uri uri, String[] projection, String sortOrder) {


        return QueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sFavs,
                null,
                null,
                null,
                sortOrder
        );
    }



    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = com.example.android.popularmoviesone.TestMovieContract.CONTENT_AUTHORITY;

//        static final int MOVIE_POPULAR = 100;
//        static final int MOVIE_TOP_RATED = 101;
//        static final int MOVIE_TRAILERS = 102;
//        static final int MOVIE_REVIEWS = 103;
//        static final int MOVIE_FAVS = 104;
//        static final int MY_MOVIES = 105;
//        static final int TRAILERS_REVIEWS = 106;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.ALL_MOVIE, MY_MOVIES);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.PATH_POPULAR + "/*", MOVIE_POPULAR);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.PATH_TOP_RATED + "/*", MOVIE_TOP_RATED);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.PATH_TRAILERS + "/#/*", MOVIE_TRAILERS);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.PATH_REVIEWS + "/#/*", MOVIE_REVIEWS);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.PATH_FAVS + "/#", MOVIE_FAVS);
        matcher.addURI(authority, com.example.android.popularmoviesone.TestMovieContract.ALL_MOVIE_EXTRA, TRAILERS_REVIEWS);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelperTest(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE_POPULAR:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieList.CONTENT_TYPE;
            case MOVIE_TOP_RATED:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieList.CONTENT_TYPE;
            case MOVIE_TRAILERS:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.CONTENT_ITEM_TYPE;
            case MOVIE_REVIEWS:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.CONTENT_ITEM_TYPE;
            case MOVIE_FAVS:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieList.CONTENT_TYPE;
            case MY_MOVIES:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieList.CONTENT_TYPE;
            case TRAILERS_REVIEWS:
                return com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.CONTENT_TYPE;
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
            // "MOVIE/*"
            case MOVIE_TOP_RATED: {
                retCursor = getTopRated(uri, projection, sortOrder);
                break;
            }
            // "MOVIE/#/*"
            case MOVIE_TRAILERS: {
                retCursor = getTrailers(uri, projection, sortOrder);
                break;
            }

            // "MOVIE/#/*"
            case MOVIE_REVIEWS: {
                retCursor = getReviews(uri, projection, sortOrder);
                break;
            }
            // "MOVIE/#"
            case MOVIE_FAVS: {
                retCursor = getFavs(uri, projection, sortOrder);
                break;
            }
            // "MY_MOVIES"
            case MY_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "TRAILERS_REVIEWS"
            case TRAILERS_REVIEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MY_MOVIES: {
                long _id = db.insert(com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = com.example.android.popularmoviesone.TestMovieContract.TheMovieList.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS_REVIEWS: {
                long _id = db.insert(com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.buildFavUri(_id);
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
                        com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS_REVIEWS:
                rowsDeleted = db.delete(
                        com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME, selection, selectionArgs);
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
                rowsUpdated = db.update(com.example.android.popularmoviesone.TestMovieContract.TheMovieList.TABLE_NAME, null, selection,
                        selectionArgs);
                break;
            case TRAILERS_REVIEWS:
                rowsUpdated = db.update(com.example.android.popularmoviesone.TestMovieContract.TheMovieExtras.TABLE_NAME, null, selection,
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
            case MY_MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TestMovieContract.TheMovieList.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
