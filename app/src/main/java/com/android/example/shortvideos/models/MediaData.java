package com.android.example.shortvideos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import androidx.annotation.NonNull;

public class MediaData {
    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("video")
    @Expose
    private String video;

    @SerializedName("thum")
    @Expose
    private String thum;

    @SerializedName("gif")
    @Expose
    private String gif;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getThum() {
        return thum;
    }

    public void setThum(String thum) {
        this.thum = thum;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    @NonNull
    @Override
    public String toString() {
        return "Msg{" +
                "uid='" + uid + '\'' +
                ", _id='" + _id + '\'' +
                ", video='" + video + '\'' +
                ", thum='" + thum + '\'' +
                ", gif='" + gif + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaData mediaData = (MediaData) o;
        return uid.equals(mediaData.uid) && _id.equals(mediaData._id) && video.equals(mediaData.video) && thum.equals(mediaData.thum) && gif.equals(mediaData.gif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, _id, video, thum, gif);
    }
}
