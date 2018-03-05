package com.shariqansasri.movieapp.activities;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.shariqansasri.movieapp.adapter.MoviesAdapter;
import com.shariqansasri.movieapp.adapter.TvAdapter;
import com.shariqansasri.movieapp.json.Endpoints;
import com.shariqansasri.movieapp.pojo.Movie;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.endpoints.EndpointUrls;
import com.shariqansasri.movieapp.endpoints.EndpointsParameters;
import com.shariqansasri.movieapp.endpoints.Keys;
import com.shariqansasri.movieapp.extras.Constants;
import com.shariqansasri.movieapp.logging.L;
import com.shariqansasri.movieapp.network.VolleySingleton;
import com.shariqansasri.movieapp.pojo.Tv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.tatarka.support.job.JobScheduler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MoviesAdapter.OnMovieCliclListener, TvAdapter.OnTvClickListener {

    private static final String KEY_PARCELABLE_LIST = "parcelable_array_list";
    //    Android fields...
    private Toolbar toolbarMain;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private TextView textViewAppBarTitle;
    private CircularProgressBar circularProgressBar;

    //    Font field...
    private Typeface typeface;

    //    Instance fields...
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<Movie> moviesArrayList = new ArrayList<>();
    ArrayList<Tv> tvArrayList = new ArrayList<>();

    //    Adapter fields...
    private MoviesAdapter moviesAdapter;
    private TvAdapter tvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Android fields initialization...
        toolbarMain = findViewById(R.id.appBarMain);
        navigationView = findViewById(R.id.navigationViewMain);
        recyclerView = findViewById(R.id.recyclerViewMain);
        drawerLayout = findViewById(R.id.drawerLayoutMain);
        textViewAppBarTitle = toolbarMain.findViewById(R.id.appBarTitle);
        circularProgressBar = findViewById(R.id.progressBarHome);

//        Set fonts...
        typeface = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        textViewAppBarTitle.setTypeface(typeface);

//        Setting toolbar...
        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        textViewAppBarTitle.setText(R.string.upcoming_movies);

//        Adapter...
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MoviesAdapter(this);
        moviesAdapter.setOnMovieCliclListener(this);
        tvAdapter = new TvAdapter(this);
        tvAdapter.setOnTvClickListener(this);
        recyclerView.setAdapter(moviesAdapter);
//        Json requests...
        if (savedInstanceState == null) {
            sendJsonRequest(EndpointUrls.UPCOMING_MOVIES_URL, "en-US", 1);
        } else {
            moviesArrayList = savedInstanceState.getParcelableArrayList(KEY_PARCELABLE_LIST);
            moviesAdapter.setMovieList(moviesArrayList);
        }

//        Setting navigation view...
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarMain, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.getMenu().getItem(1).setChecked(true);

//        Setting event listeners...
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_PARCELABLE_LIST, moviesArrayList);
    }

    private void sendJsonRequest(String EndpointBaseUrl, String region, int pageNumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        circularProgressBar.setStartAngle(45);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getEndpointUrl(EndpointBaseUrl, pageNumber, region), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                recyclerView.setVisibility(View.VISIBLE);
                moviesArrayList = parseJson(response);
                recyclerView.setAdapter(moviesAdapter);
                moviesAdapter.setMovieList(moviesArrayList);
                circularProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.t(MainActivity.this, error.getMessage());
                recyclerView.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getVolleySingleton().getRequestQueue().add(jsonObjectRequest);
//        recyclerView.setVisibility(View.VISIBLE);
    }

    private ArrayList<Movie> parseJson(JSONObject response) {
        ArrayList<Movie> movieList = new ArrayList<>();
        if (response != null && response.length() > 0) {
            if (checkValidationKey(response, Keys.RESULTS)) {
                try {
                    JSONArray result = response.getJSONArray(Keys.RESULTS);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject currentMovie = result.getJSONObject(i);
                        int voteCount = Constants.ID;
                        long id = Constants.ID;
                        boolean video = false;
                        double voteAverage = 0.0;
                        String title = Constants.NA;
                        double popularity = 0.0;
                        String posterPath = Constants.NA;
                        String originalLanguage = Constants.NA;
                        String originalTitle = Constants.NA;
                        List<Integer> genreId = new ArrayList<>();
                        String backDropPath = Constants.NA;
                        boolean adult = false;
                        String overview = Constants.NA;
                        String releaseDate = Constants.NA;
                        Date date = null;

                        if (checkValidationKey(currentMovie, Keys.VOTE_COUNT)) {
                            voteCount = currentMovie.getInt(Keys.VOTE_COUNT);
                        }
                        if (checkValidationKey(currentMovie, Keys.ID)) {
                            id = currentMovie.getLong(Keys.ID);
                        }
                        if (checkValidationKey(currentMovie, Keys.VIDEO)) {
                            video = currentMovie.getBoolean(Keys.VIDEO);
                        }
                        if (checkValidationKey(currentMovie, Keys.VOTE_AVERAGE)) {
                            voteAverage = currentMovie.getDouble(Keys.VOTE_AVERAGE);
                        }
                        if (checkValidationKey(currentMovie, Keys.TITLE)) {
                            title = currentMovie.getString(Keys.TITLE);
                        }
                        if (checkValidationKey(currentMovie, Keys.POPULARITY)) {
                            popularity = currentMovie.getDouble(Keys.POPULARITY);
                        }
                        if (checkValidationKey(currentMovie, Keys.POSTER_PATH)) {
                            posterPath = currentMovie.getString(Keys.POSTER_PATH);
                        }
                        if (checkValidationKey(currentMovie, Keys.ORIGINAL_LANGUAGE)) {
                            originalLanguage = currentMovie.getString(Keys.ORIGINAL_LANGUAGE);
                        }
                        if (checkValidationKey(currentMovie, Keys.ORIGINAL_TITLE)) {
                            originalTitle = currentMovie.getString(Keys.ORIGINAL_TITLE);
                        }
                        if (checkValidationKey(currentMovie, Keys.GENRE_ID)) {
                            JSONArray genreArray = currentMovie.getJSONArray(Keys.GENRE_ID);
                            if (genreArray.length() > 0) {
                                for (int j = 0; j < genreArray.length(); j++) {
                                    genreId.add(genreArray.getInt(j));
                                }
                            }
                        }
                        if (checkValidationKey(currentMovie, Keys.BACKDROP_PATH)) {
                            backDropPath = currentMovie.getString(Keys.BACKDROP_PATH);
                        }
                        if (checkValidationKey(currentMovie, Keys.ADULT)) {
                            adult = currentMovie.getBoolean(Keys.ADULT);
                        }
                        if (checkValidationKey(currentMovie, Keys.OVERVIEW)) {
                            overview = currentMovie.getString(Keys.OVERVIEW);
                        }
                        if (checkValidationKey(currentMovie, Keys.RELEASE_DATE)) {
                            releaseDate = currentMovie.getString(Keys.RELEASE_DATE);
                            date = dateFormat.parse(releaseDate);
                        }
                        if (date != null && id != Constants.ID) {
                            Movie movie = new Movie(voteCount, id, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, genreId, backDropPath, adult, overview, date);
                            movieList.add(movie);
                        }
                    }
                } catch (JSONException e) {
                    L.t(this, e.getMessage());
                } catch (ParseException e) {
                    L.t(this, e.getMessage());
                }
            }
        }
        return movieList;
    }

    public boolean checkValidationKey(JSONObject response, String key) {
        return response.has(key) && !response.isNull(key);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSearch:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
        }
        return false;
    }

    private void sendTvJsonRequest(String url, int pageNumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        circularProgressBar.setStartAngle(45);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getEndpointUrl(url, pageNumber, "en-US"), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                recyclerView.setVisibility(View.VISIBLE);
                tvArrayList = parseTvJson(response);
                recyclerView.setAdapter(tvAdapter);
                tvAdapter.setTvList(tvArrayList);
                circularProgressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.t(MainActivity.this, error.getMessage());
                recyclerView.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getVolleySingleton().getRequestQueue().add(jsonObjectRequest);
    }

    private ArrayList<Tv> parseTvJson(JSONObject response) {
        ArrayList<Tv> tvArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {
            if (checkValidationKey(response, Keys.RESULTS)) {
                try {
                    JSONArray result = response.getJSONArray(Keys.RESULTS);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject currentTv = result.getJSONObject(i);
                        int voteCount = Constants.ID;
                        long id = Constants.ID;
                        double voteAverage = 0.0;
                        String name = Constants.NA;
                        double popularity = 0.0;
                        String posterPath = Constants.NA;
                        String originalLanguage = Constants.NA;
                        String originalName = Constants.NA;
                        ArrayList<Integer> genreId = new ArrayList<>();
                        ArrayList<String> originCountry = new ArrayList<>();
                        String backDropPath = Constants.NA;
                        String overview = Constants.NA;
                        String firstAirDate = Constants.NA;
                        Date date = null;

                        if (checkValidationKey(currentTv, Keys.VOTE_COUNT)) {
                            voteCount = currentTv.getInt(Keys.VOTE_COUNT);
                        }
                        if (checkValidationKey(currentTv, Keys.ID)) {
                            id = currentTv.getLong(Keys.ID);
                        }
                        if (checkValidationKey(currentTv, Keys.VOTE_AVERAGE)) {
                            voteAverage = currentTv.getDouble(Keys.VOTE_AVERAGE);
                        }
                        if (checkValidationKey(currentTv, Keys.NAME)) {
                            name = currentTv.getString(Keys.NAME);
                        }
                        if (checkValidationKey(currentTv, Keys.POPULARITY)) {
                            popularity = currentTv.getDouble(Keys.POPULARITY);
                        }
                        if (checkValidationKey(currentTv, Keys.POSTER_PATH)) {
                            posterPath = currentTv.getString(Keys.POSTER_PATH);
                        }
                        if (checkValidationKey(currentTv, Keys.ORIGINAL_LANGUAGE)) {
                            originalLanguage = currentTv.getString(Keys.ORIGINAL_LANGUAGE);
                        }
                        if (checkValidationKey(currentTv, Keys.ORIGINAL_NAME)) {
                            originalName = currentTv.getString(Keys.ORIGINAL_NAME);
                        }
                        if (checkValidationKey(currentTv, Keys.GENRE_ID)) {
                            JSONArray genreArray = currentTv.getJSONArray(Keys.GENRE_ID);
                            if (genreArray.length() > 0) {
                                for (int j = 0; j < genreArray.length(); j++) {
                                    genreId.add(genreArray.getInt(j));
                                }
                            }
                        }
                        if (checkValidationKey(currentTv, Keys.ORIGIN_COUNTRY)) {
                            JSONArray genreArray = currentTv.getJSONArray(Keys.ORIGIN_COUNTRY);
                            if (genreArray.length() > 0) {
                                for (int j = 0; j < genreArray.length(); j++) {
                                    originCountry.add(genreArray.getString(j));
                                }
                            }
                        }
                        if (checkValidationKey(currentTv, Keys.BACKDROP_PATH)) {
                            backDropPath = currentTv.getString(Keys.BACKDROP_PATH);
                        }
                        if (checkValidationKey(currentTv, Keys.OVERVIEW)) {
                            overview = currentTv.getString(Keys.OVERVIEW);
                        }
                        if (checkValidationKey(currentTv, Keys.FIRST_AIR_DATE)) {
                            firstAirDate = currentTv.getString(Keys.FIRST_AIR_DATE);
                            date = dateFormat.parse(firstAirDate);
                        }
                        if (date != null && id != Constants.ID) {
                            Tv tv = new Tv(originalName, genreId, name, popularity, originCountry, voteCount, date, backDropPath, originalLanguage, id, voteAverage, overview, posterPath);
                            tvArrayList.add(tv);
                        }
                    }
                } catch (JSONException e) {
                    L.t(this, e.getMessage());
                } catch (ParseException e) {
                    L.t(this, e.getMessage());
                }
            }
        }
        return tvArrayList;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.menuItemUpcomingMovies:
                textViewAppBarTitle.setText(R.string.upcoming_movies);
                sendJsonRequest(EndpointUrls.UPCOMING_MOVIES_URL, "en-US", 1);
                return true;
            case R.id.menuItemPopularMovies:
                textViewAppBarTitle.setText(R.string.popular_movies);
                sendJsonRequest(EndpointUrls.POPULAR_MOVIES_URL, "en-US", 1);
                return true;
            case R.id.menuItemTopRatedMovies:
                textViewAppBarTitle.setText(R.string.top_rated_movies);
                sendJsonRequest(EndpointUrls.TOP_RATED_MOVIES_URL, "en-US", 1);
                return true;
            case R.id.menuItemLatestMovies:
                textViewAppBarTitle.setText(R.string.latest_movies);
                sendJsonRequest(EndpointUrls.LATEST_MOVIES_URL, "en-US", 1);
                return true;
            case R.id.menuItemLatestTv:
                textViewAppBarTitle.setText(R.string.latest_tv_shows);
                return true;
            case R.id.menuItemPopularTv:
                textViewAppBarTitle.setText(R.string.popular_tv_shows);
                sendTvJsonRequest(EndpointUrls.POPULAR_TV_URL, 1);
                return true;
            case R.id.menuItemTopRatedTv:
                textViewAppBarTitle.setText(R.string.top_rated_tv_shows);
                sendTvJsonRequest(EndpointUrls.TOP_RATED_TV_URL, 1);
                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public String getEndpointUrl(String EndpointBaseUrl, int pageNumber, String region) {
        return EndpointBaseUrl
                + EndpointsParameters.QUESTION
                + EndpointsParameters.API
                + EndpointsParameters.EQUAL
                + EndpointsParameters.API_KEY
                + EndpointsParameters.EMPERSAND
                + EndpointsParameters.LANGUAGE
                + EndpointsParameters.EQUAL
                + region
                + EndpointsParameters.EMPERSAND
                + EndpointsParameters.PAGE
                + EndpointsParameters.EQUAL
                + pageNumber;
    }

    @Override
    public void onMovieClick(View view, int index, long movieId) {
        startDetailActivity(movieId, "movies");
    }

    @Override
    public void onTvClick(View view, int index, long id) {
        startDetailActivity(id, "tv");
    }

    private void startDetailActivity(long id, String type) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Keys.ID, id);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
