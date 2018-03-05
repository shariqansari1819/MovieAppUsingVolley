package com.shariqansasri.movieapp.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.fragments.MovieSearchFragment;
import com.shariqansasri.movieapp.fragments.TvSearchFragment;

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener, TextWatcher {

    //    Android fields...
    private Toolbar toolbarSearch;
    private EditText editTextSearch;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //    Interface callbacks...
    OnTextSearchGetListener onTextSearchGetListener;

    //    Pager adapter fields...
    private SearchPagerAdapter searchPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        Android fields initialization...
        toolbarSearch = findViewById(R.id.appBarSearch);
        editTextSearch = findViewById(R.id.editTextSearch);
        tabLayout = findViewById(R.id.tabLayoutSearch);
        viewPager = findViewById(R.id.viewPagerSearch);

//        Setting toolbar...
        setSupportActionBar(toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        Setting tabs...
        searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(searchPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        editTextSearch.setOnEditorActionListener(this);
        editTextSearch.addTextChangedListener(this);

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            Toast.makeText(SearchActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (onTextSearchGetListener != null) {
            onTextSearchGetListener.getSearchText(charSequence.toString().trim());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    class SearchPagerAdapter extends FragmentStatePagerAdapter {

        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MovieSearchFragment();
                    break;
                case 1:
                    fragment = new TvSearchFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "Movies";
                    break;
                case 1:
                    title = "Tv Shows";
                    break;
            }
            return title;
        }
    }

    public void setSearchListener(OnTextSearchGetListener onTextSearchGetListener) {
        this.onTextSearchGetListener = onTextSearchGetListener;
    }

    public interface OnTextSearchGetListener {
        void getSearchText(String text);
    }

}