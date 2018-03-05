package com.shariqansasri.movieapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.endpoints.EndpointUrls;
import com.shariqansasri.movieapp.endpoints.EndpointsParameters;
import com.shariqansasri.movieapp.endpoints.Keys;
import com.shariqansasri.movieapp.extras.Constants;
import com.shariqansasri.movieapp.fragments.GenreFragment;
import com.shariqansasri.movieapp.fragments.InfoFragment;
import com.shariqansasri.movieapp.fragments.ReviewFragment;
import com.shariqansasri.movieapp.logging.L;
import com.shariqansasri.movieapp.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    //    Android fields...
    private YouTubePlayerView youTubePlayerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //    Instance variables...
    private long id;
    private String type;

    //    Tab Adapter...
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Getting intent from main activity...
        Intent intent = getIntent();
        id = intent.getLongExtra(Keys.ID, -1);
        type = intent.getStringExtra("type");
//        Android fields initialization...
        youTubePlayerView = findViewById(R.id.youtubePlayer);
        tabLayout = findViewById(R.id.tabLayoutDetail);
        viewPager = findViewById(R.id.viewPagerDetail);

//        tab adapter...
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        Better video player code...
        String baseUrl = Constants.NA;
        if (id != -1) {
            if (type.equals("movies")) {
                baseUrl = EndpointUrls.VIDEO_MOVIE_URL;
            } else if (type.equals("tv")) {
                baseUrl = EndpointUrls.VIDEO_TV_URL;
            }
            if (!baseUrl.equals(Constants.NA))
                sendVideoJsonRequest(baseUrl, "en-US", id);
        }
    }

    private void sendVideoJsonRequest(String baseUrl, String region, long id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getVideoUrl(baseUrl, id, region), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final String url = parseVideoJson(response);
                youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                    @Override
                    public void onInitSuccess(final YouTubePlayer youTubePlayer) {
                        youTubePlayer.addListener(new YouTubePlayerListener() {
                            @Override
                            public void onReady() {
                                youTubePlayer.loadVideo(url, 0);
                            }

                            @Override
                            public void onStateChange(int state) {

                            }

                            @Override
                            public void onPlaybackQualityChange(String playbackQuality) {

                            }

                            @Override
                            public void onPlaybackRateChange(String playbackRate) {

                            }

                            @Override
                            public void onError(int error) {

                            }

                            @Override
                            public void onApiChange() {

                            }

                            @Override
                            public void onCurrentSecond(float second) {

                            }

                            @Override
                            public void onVideoDuration(float duration) {

                            }

                            @Override
                            public void onMessage(String log) {

                            }

                            @Override
                            public void onVideoId(String videoId) {

                            }
                        });
                    }
                }, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.t(DetailActivity.this, error.getMessage());
            }
        });
        VolleySingleton.getVolleySingleton().getRequestQueue().add(jsonObjectRequest);
    }

    private String parseVideoJson(JSONObject response) {
        String url = Constants.NA;
        if (response != null && response.length() > 0) {
            if (response.has(Keys.RESULTS) && !response.isNull(Keys.RESULTS)) {
                try {
                    JSONArray jsonArray = response.getJSONArray(Keys.RESULTS);
                    JSONObject currentVideo = jsonArray.getJSONObject(0);
                    if (currentVideo.has("key") && !currentVideo.isNull("key")) {
                        url = currentVideo.getString("key");
                    }
                } catch (JSONException e) {
                    L.t(DetailActivity.this, e.getMessage());
                }
            }
        }
        return url;
    }

    public String getVideoUrl(String EndpointBaseUrl, long id, String region) {
        return EndpointBaseUrl
                + id
                + "/videos"
                + EndpointsParameters.QUESTION
                + EndpointsParameters.API
                + EndpointsParameters.EQUAL
                + EndpointsParameters.API_KEY
                + EndpointsParameters.EMPERSAND
                + EndpointsParameters.LANGUAGE
                + EndpointsParameters.EQUAL
                + region;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new InfoFragment();
                    break;
                case 1:
                    fragment = new GenreFragment();
                    break;
                case 2:
                    fragment = new ReviewFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "Info";
                    break;
                case 1:
                    title = "Genre";
                    break;
                case 2:
                    title = "Reviews";
                    break;
            }
            return title;
        }
    }
}