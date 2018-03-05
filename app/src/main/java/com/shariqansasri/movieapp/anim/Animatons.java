package com.shariqansasri.movieapp.anim;

import android.support.v7.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Animatons {
    public static void animateRecyclerView(RecyclerView.ViewHolder viewHolder) {
        YoYo.with(Techniques.Landing)
                .duration(900)
                .playOn(viewHolder.itemView);
    }
}
