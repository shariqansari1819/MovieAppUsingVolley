package com.shariqansasri.movieapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shariqansasri.movieapp.R;
import com.shariqansasri.movieapp.anim.Animatons;
import com.shariqansasri.movieapp.pojo.Movie;
import com.shariqansasri.movieapp.pojo.Tv;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.TvViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Tv> tvArrayList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private OnTvClickListener onTvClickListener;

    public TvAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnTvClickListener(OnTvClickListener onTvClickListener) {
        this.onTvClickListener = onTvClickListener;
    }

    public void setTvList(List<Tv> tvList) {
        notifyItemRangeRemoved(0, tvArrayList.size());
        tvArrayList = tvList;
        notifyItemRangeChanged(0, tvList.size());
    }

    @Override
    public TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvViewHolder(layoutInflater.inflate(R.layout.layout_main_row, parent, false));
    }

    @Override
    public void onBindViewHolder(TvAdapter.TvViewHolder holder, int position) {
        Tv tv = tvArrayList.get(position);
        String title = tv.getName();
        String poster = tv.getPosterPath();
        double rating = tv.getVoteAverage();
        Date date = tv.getFirstAirDate();

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185//" + poster)
                .placeholder(R.drawable.zootopia_thumbnail)
                .error(R.drawable.zootopia_thumbnail)
                .into(holder.imageViewThumbnail);
        holder.textViewTitle.setText(title);
        holder.textViewYear.setText(dateFormat.format(date));
        holder.textViewVotes.setText(String.valueOf(rating * 10));
        Animatons.animateRecyclerView(holder);
    }

    @Override
    public int getItemCount() {
        return tvArrayList.size();
    }

    class TvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_regular.otf");
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_bold.otf");

        ImageView imageViewThumbnail;
        TextView textViewTitle, textViewYear, textViewVotes, textViewAudiance;

        public TvViewHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnailHomeRow);
            textViewTitle = itemView.findViewById(R.id.textViewMovieTitleHomeRow);
            textViewYear = itemView.findViewById(R.id.textViewMovieYearHomeRow);
            textViewVotes = itemView.findViewById(R.id.textViewMovieRatingHomeRow);
            textViewAudiance = itemView.findViewById(R.id.textViewMovieAudienceHomeRow);
            textViewTitle.setTypeface(typefaceBold);
            textViewYear.setTypeface(typefaceRegular);
            textViewVotes.setTypeface(typefaceBold);
            textViewAudiance.setTypeface(typefaceRegular);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onTvClickListener != null)
                onTvClickListener.onTvClick(view, getAdapterPosition(), tvArrayList.get(getAdapterPosition()).getId());
        }
    }

    public interface OnTvClickListener {
        public void onTvClick(View view, int index, long id);
    }
}
