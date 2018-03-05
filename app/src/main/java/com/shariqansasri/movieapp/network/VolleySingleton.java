package com.shariqansasri.movieapp.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.shariqansasri.movieapp.app.MyApplication;

public class VolleySingleton {

    private static VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingleton getVolleySingleton() {
        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton();
        }
        return volleySingleton;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void addRequestToQueue(Request request) {
        requestQueue.add(request);
    }
}
