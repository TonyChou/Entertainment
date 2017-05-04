package com.union.player;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouxiaming on 2017/5/3.
 */

public class MusicInfo implements Parcelable {
    private String musicId;
    private String trackName;
    private String musicTitle;
    private String musicUrl;
    private String musicCover;

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicCover() {
        return musicCover;
    }

    public void setMusicCover(String musicCover) {
        this.musicCover = musicCover;
    }
    public MusicInfo() {

    }

    protected MusicInfo(Parcel in) {
    }

    private void readFromParcel(Parcel in) {
        musicId = in.readString();
        trackName = in.readString();
        musicTitle = in.readString();
        musicUrl = in.readString();
        musicCover = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(musicId);
        dest.writeString(trackName);
        dest.writeString(musicTitle);
        dest.writeString(musicUrl);
        dest.writeString(musicCover);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
}
