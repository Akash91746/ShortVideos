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

    @SerializedName("user_info")
    @Expose
    private UserInfo user_info;

    @SerializedName("count")
    @Expose
    private Count count;

    @SerializedName("description")
    @Expose
    private String description;

    public String getUid() {
        return uid;
    }


    public String get_id() {
        return _id;
    }


    public String getVideo() {
        return video;
    }


    public String getThum() {
        return thum;
    }


    public String getGif() {
        return gif;
    }

    public UserInfo getUser_info() {
        return user_info;
    }

    public Count getCount() {
        return count;
    }

    public String getDescription() {
        return description;
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
        if (!(o instanceof MediaData)) return false;
        MediaData mediaData = (MediaData) o;
        return uid.equals(mediaData.uid) && _id.equals(mediaData._id) && video.equals(mediaData.video) && Objects.equals(thum, mediaData.thum) && Objects.equals(gif, mediaData.gif) && user_info.equals(mediaData.user_info) && count.equals(mediaData.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, _id, video, thum, gif, user_info, count);
    }
}
