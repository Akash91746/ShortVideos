package com.android.example.shortvideos.repositories;

import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.models.VideoData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class VideoRepository {

    private final VideosApi videosApi;

    public VideoRepository(VideosApi videosApi) {
        this.videosApi = videosApi;
    }

    public MutableLiveData<List<MediaData>> fetchData() {
        MutableLiveData<List<MediaData>> data = new MutableLiveData<>();

        Call<VideoData> response = videosApi.getVideoData();

        response.enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(@NonNull Call<VideoData> call, @NonNull Response<VideoData> response) {
                Timber.d("onReceive: Server response : %s", response.toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        data.postValue(response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoData> call, @NonNull Throwable t) {
                Timber.e("onFailure : %s", t.getMessage());
            }
        });

        return data;
    }

}
