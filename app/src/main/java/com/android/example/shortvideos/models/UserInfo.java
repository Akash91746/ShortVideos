package com.android.example.shortvideos.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class UserInfo {
    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("username")
    @Expose
    private String username;

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return first_name.equals(userInfo.first_name) && Objects.equals(last_name, userInfo.last_name) && Objects.equals(profile_pic, userInfo.profile_pic) && _id.equals(userInfo._id) && username.equals(userInfo.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, last_name, profile_pic, _id, username);
    }
}

