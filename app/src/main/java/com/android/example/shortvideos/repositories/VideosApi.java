package com.android.example.shortvideos.repositories;

import com.android.example.shortvideos.models.VideoData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideosApi {

    @GET("app_api/index.php?p=showAllVideos")
    Call<VideoData> getVideoData();
}
