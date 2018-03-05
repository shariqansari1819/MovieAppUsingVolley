package com.shariqansasri.movieapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tv implements Parcelable {
    private String originalName;
    private ArrayList<Integer> genreList;
    private String name;
    private double popularity;
    private ArrayList<String> originCountry;
    private int voteCount;
    private Date firstAirDate;
    private String backDropPath;
    private String originalLanguage;
    private long id;
    private double voteAverage;
    private String overView;
    private String posterPath;

    public Tv(String originalName, ArrayList<Integer> genreList, String name, double popularity, ArrayList<String> originCountry, int voteCount, Date firstAirDate, String backDropPath, String originalLanguage, long id, double voteAverage, String overView, String posterPath) {
        this.originalName = originalName;
        this.genreList = genreList;
        this.name = name;
        this.popularity = popularity;
        this.originCountry = originCountry;
        this.voteCount = voteCount;
        this.firstAirDate = firstAirDate;
        this.backDropPath = backDropPath;
        this.originalLanguage = originalLanguage;
        this.id = id;
        this.voteAverage = voteAverage;
        this.overView = overView;
        this.posterPath = posterPath;
    }

    protected Tv(Parcel in) {
        originalName = in.readString();
        name = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        try {
            firstAirDate = new SimpleDateFormat("yyyy-MM-dd").parse(in.readString());
        } catch (ParseException e) {

        }
        backDropPath = in.readString();
        originalLanguage = in.readString();
        id = in.readLong();
        voteAverage = in.readDouble();
        overView = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel in) {
            return new Tv(in);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public ArrayList<Integer> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<Integer> genreList) {
        this.genreList = genreList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public ArrayList<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(ArrayList<String> originCountry) {
        this.originCountry = originCountry;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Date getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(Date firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalName);
        parcel.writeString(name);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeString(new SimpleDateFormat("yyyy-MM-dd").format(firstAirDate));
        parcel.writeString(backDropPath);
        parcel.writeString(originalLanguage);
        parcel.writeLong(id);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overView);
        parcel.writeString(posterPath);

    }
}
