package com.shariqansasri.movieapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.activities.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieSearchFragment extends Fragment implements SearchActivity.OnTextSearchGetListener {


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
        return view;
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
