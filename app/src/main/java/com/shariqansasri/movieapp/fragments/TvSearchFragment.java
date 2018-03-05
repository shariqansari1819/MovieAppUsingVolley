package com.shariqansasri.movieapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class TvSearchFragment extends Fragment implements SearchActivity.OnTextSearchGetListener {


    public TvSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_search, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SearchActivity searchActivity = (SearchActivity) getActivity();
        if (searchActivity != null)
            searchActivity.setSearchListener(this);
    }

    @Override
    public void getSearchText(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}