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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Movie> movieArrayList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private OnMovieCliclListener onMovieCliclListener;

    public MoviesAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setOnMovieCliclListener(OnMovieCliclListener onMovieCliclListener) {
        this.onMovieCliclListener = onMovieCliclListener;
    }

    public void setMovieList(List<Movie> movieList) {
        notifyItemRangeRemoved(0, movieArrayList.size());
        movieArrayList = movieList;
        notifyItemRangeChanged(0, movieList.size());
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(layoutInflater.inflate(R.layout.layout_main_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        String title = movie.getTitle();
        String poster = movie.getPosterPath();
        double rating = movie.getVoteAverage();
        Date date = movie.getReleaseDate();

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
        return movieArrayList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_regular.otf");
        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_bold.otf");

        ImageView imageViewThumbnail;
        TextView textViewTitle, textViewYear, textViewVotes, textViewAudiance;

        public MovieViewHolder(View itemView) {
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
            if (onMovieCliclListener != null)
                onMovieCliclListener.onMovieClick(view, getAdapterPosition(), movieArrayList.get(getAdapterPosition()).getId());
        }
    }

    public interface OnMovieCliclListener {
        void onMovieClick(View view, int index, long movieId);
    }
}
