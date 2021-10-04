package com.android.example.shortvideos.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Count {
    @SerializedName("like_count")
    @Expose
    private int like_count;

    @SerializedName("video_comment_count")
    @Expose
    private int video_comment_count;

    @SerializedName("view")
    @Expose
    private int view;

    @SerializedName("_id")
    @Expose
    private String _id;

    public int getLike_count() {
        return like_count;
    }

    public int getVideo_comment_count() {
        return video_comment_count;
    }

    public int getView() {
        return view;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Count count = (Count) o;
        return like_count == count.like_count && video_comment_count == count.video_comment_count && view == count.view && _id.equals(count._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(like_count, video_comment_count, view, _id);
    }
}