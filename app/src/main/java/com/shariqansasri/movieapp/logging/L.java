package com.shariqansasri.movieapp.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
    public static void t(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void l(String message) {
        Log.e("Movie", message);
    }
}
