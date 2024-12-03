package com.example.bumaga2;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private String title;
    private String developer;
    private Bitmap image;

    public Game() {}

    public Game(String title, String developer, Bitmap image) {
        this.title = title;
        this.developer = developer;
        this.image = image;
    }

    protected Game(Parcel in) {
        title = in.readString();
        developer = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(developer);
        dest.writeParcelable(image, flags);
    }

    @Override
    public String toString() {
        return title + " by " + developer;
    }
}