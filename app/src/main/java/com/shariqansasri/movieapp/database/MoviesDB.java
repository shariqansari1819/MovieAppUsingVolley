package com.shariqansasri.movieapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.shariqansasri.movieapp.logging.L;
import com.shariqansasri.movieapp.pojo.Movie;

import java.util.ArrayList;
import java.util.Date;

public class MoviesDB {

    private MoviesHelper moviesHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MoviesDB(Context context) {
        moviesHelper = new MoviesHelper(context);
        sqLiteDatabase = moviesHelper.getWritableDatabase();
    }

    public void insertMovies(ArrayList<Movie> movieArrayList, boolean isDeleteData) {
        if (isDeleteData) {
            deleteData();
        }
        String sql = "INSERT INTO " + MoviesHelper.TABLE_UPCOMING + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();
        for (int i = 0; i < movieArrayList.size(); i++) {
            Movie currentMovie = movieArrayList.get(i);
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindLong(2, currentMovie.getId());
            sqLiteStatement.bindLong(3, currentMovie.getVoteCount());
            sqLiteStatement.bindLong(4, currentMovie.isVideo() ? 1 : 0);
            sqLiteStatement.bindDouble(5, currentMovie.getVoteAverage());
            sqLiteStatement.bindString(6, currentMovie.getTitle());
            sqLiteStatement.bindDouble(7, currentMovie.getPopularity());
            sqLiteStatement.bindString(8, currentMovie.getPosterPath());
            sqLiteStatement.bindString(9, currentMovie.getOriginalLanguage());
            sqLiteStatement.bindString(10, currentMovie.getOriginalTitle());
            sqLiteStatement.bindString(11, currentMovie.getBackdropPath());
            sqLiteStatement.bindLong(12, currentMovie.isAdult() ? 1 : 0);
            sqLiteStatement.bindString(13, currentMovie.getOverview());
            sqLiteStatement.bindLong(14, currentMovie.getReleaseDate() == null ? -1 : currentMovie.getReleaseDate().getTime());
            sqLiteStatement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Movie> readMoviesData() {
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        String[] columns = {
                MoviesHelper.ID,
                MoviesHelper.VOTE_COUNT,
                MoviesHelper.VIDEO,
                MoviesHelper.VOTE_AVERAGE,
                MoviesHelper.TITLE,
                MoviesHelper.POPULARITY,
                MoviesHelper.POSTER_PATH,
                MoviesHelper.ORIGINAL_LANGUAGE,
                MoviesHelper.ORIGINAL_TITLE,
                MoviesHelper.BACKDROP_PATH,
                MoviesHelper.ADULT,
                MoviesHelper.OVERVIEW,
                MoviesHelper.RELEASE_DATE
        };
        Cursor cursor = sqLiteDatabase.query(MoviesHelper.TABLE_UPCOMING, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MoviesHelper.ID));
                int voteCount = cursor.getInt(cursor.getColumnIndex(MoviesHelper.VOTE_COUNT));
                boolean videoData = cursor.getLong(cursor.getColumnIndex(MoviesHelper.VIDEO)) == 1;
                double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesHelper.VOTE_AVERAGE));
                String title = cursor.getString(cursor.getColumnIndex(MoviesHelper.TITLE));
                double popularity = cursor.getDouble(cursor.getColumnIndex(MoviesHelper.POPULARITY));
                String posterPath = cursor.getString(cursor.getColumnIndex(MoviesHelper.POSTER_PATH));
                String originalLanguage = cursor.getString(cursor.getColumnIndex(MoviesHelper.ORIGINAL_LANGUAGE));
                String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesHelper.ORIGINAL_TITLE));
                String backDropPath = cursor.getString(cursor.getColumnIndex(MoviesHelper.BACKDROP_PATH));
                boolean adult = cursor.getLong(cursor.getColumnIndex(MoviesHelper.ADULT)) == 1;
                String overview = cursor.getString(cursor.getColumnIndex(MoviesHelper.OVERVIEW));
                long date = cursor.getLong(cursor.getColumnIndex(MoviesHelper.RELEASE_DATE));
                Date date1 = date == -1 ? null : new Date(date);
                Movie movie = new Movie(voteCount, id, videoData, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, backDropPath, adult, overview, date1);
                movieArrayList.add(movie);
            }
        }
        if (cursor != null)
            cursor.close();
        return movieArrayList;
    }

    private void deleteData() {
        sqLiteDatabase.delete(MoviesHelper.TABLE_UPCOMING, null, null);
    }

    private static class MoviesHelper extends SQLiteOpenHelper {

        public static final String TABLE_UPCOMING = "movies_upcoming";
        public static final String VOTE_COUNT = "vote_count";
        public static final String COLUMN_ID = "column_id";
        public static final String ID = "id";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String ADULT = "adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";

        private static final String CREATE_TABLE_UPCOMING = "CREATE TABLE " + TABLE_UPCOMING + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID + " INTEGER," +
                VOTE_COUNT + " INTEGER," +
                VIDEO + " INTEGER," +
                VOTE_AVERAGE + " DOUBLE," +
                TITLE + " VARCHAR(255)," +
                POPULARITY + " DOUBLE," +
                POSTER_PATH + " TEXT," +
                ORIGINAL_LANGUAGE + " VARCHAR(255)," +
                ORIGINAL_TITLE + " VARCHAR(255)," +
                BACKDROP_PATH + " TEXT," +
                ADULT + " INTEGER," +
                OVERVIEW + " TEXT," +
                RELEASE_DATE + " INTEGER" + ");";

        private static final String DB_NAME = "movies_db";
        private static final int DB_VERSION = 1;
        private Context context;

        public MoviesHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_UPCOMING);
            } catch (SQLiteException e) {
                L.l(e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_UPCOMING);
            } catch (SQLiteException e) {
                L.l(e.getMessage());
            }
        }
    }
}
