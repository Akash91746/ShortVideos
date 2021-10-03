package com.android.example.shortvideos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

public class VideoData {
    @SerializedName("s")
    @Expose
    private String s;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("msg")
    @Expose
    private List<MediaData> mediaData;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<MediaData> getMsg() {
        return mediaData;
    }

    public void setMsg(List<MediaData> mediaData) {
        this.mediaData = mediaData;
    }

    @NonNull
    @Override
    public String toString() {
        return "VideoData{" +
                "s='" + s + '\'' +
                ", code='" + code + '\'' +
                ", msg=" + mediaData +
                '}';
    }
}
