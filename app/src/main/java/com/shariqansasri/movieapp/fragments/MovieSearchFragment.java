package com.shariqansasri.movieapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.activities.DetailActivity;
import com.shariqansasri.movieapp.activities.MainActivity;
import com.shariqansasri.movieapp.activities.SearchActivity;
import com.shariqansasri.movieapp.adapter.MoviesAdapter;
import com.shariqansasri.movieapp.endpoints.EndpointUrls;
import com.shariqansasri.movieapp.endpoints.EndpointsParameters;
import com.shariqansasri.movieapp.endpoints.Keys;
import com.shariqansasri.movieapp.extras.Constants;
import com.shariqansasri.movieapp.logging.L;
import com.shariqansasri.movieapp.network.VolleySingleton;
import com.shariqansasri.movieapp.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieSearchFragment extends Fragment implements SearchActivity.OnTextSearchGetListener, MoviesAdapter.OnMovieCliclListener {

    private CircularProgressBar circularProgressBar;
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;

    //    Instance fields...
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<Movie> moviesArrayList = new ArrayList<>();

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    public static MovieSearchFragment getInstance(CharSequence text) {
        MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
        Bundle bundle = new Bundle();
        movieSearchFragment.setArguments(bundle);
        return movieSearchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        circularProgressBar = view.findViewById(R.id.progressBarMovieSearch);
        recyclerView = view.findViewById(R.id.recyclerViewMovieSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        moviesAdapter = new MoviesAdapter(getActivity());
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setOnMovieCliclListener(this);
        return view;
    }

    private void sendJsonRequest(String EndpointBaseUrl, String region, int pageNumber, String keyword) {
        circularProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        circularProgressBar.setStartAngle(45);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getEndpointUrl(EndpointBaseUrl, pageNumber, region, keyword), new Response.Listener<JSONObject>() {
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
                L.t(getActivity(), error.getMessage());
                recyclerView.setVisibility(View.VISIBLE);
                circularProgressBar.setVisibility(View.GONE);
            }
        });
        VolleySingleton.getVolleySingleton().getRequestQueue().add(jsonObjectRequest);
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
                    L.t(getActivity(), e.getMessage());
                } catch (ParseException e) {
                    L.t(getActivity(), e.getMessage());
                }
            }
        }
        return movieList;
    }

    public String getEndpointUrl(String EndpointBaseUrl, int pageNumber, String region, String keyword) {
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
                + EndpointsParameters.QUERY
                + EndpointsParameters.EQUAL
                + keyword
                + EndpointsParameters.EMPERSAND
                + EndpointsParameters.PAGE
                + EndpointsParameters.EQUAL
                + pageNumber
                + EndpointsParameters.ADULT
                + EndpointsParameters.EQUAL
                + false;
    }

    public boolean checkValidationKey(JSONObject response, String key) {
        return response.has(key) && !response.isNull(key);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SearchActivity searchActivity = (SearchActivity) getActivity();
        if (searchActivity != null)
            searchActivity.setSearchListener(this);
    }

    //  This is comment...
    @Override
    public void getSearchText(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains(" ")) {
                text = text.replace(" ", "%20");
            }
            sendJsonRequest(EndpointUrls.MOVIE_SEARCH_URL, "en-US", 1, text);
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMovieClick(View view, int index, long movieId) {
        startDetailActivity(movieId, "movies");
    }

    private void startDetailActivity(long id, String type) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Keys.ID, id);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
