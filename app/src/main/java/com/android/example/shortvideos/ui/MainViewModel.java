package com.android.example.shortvideos.ui;

import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.repositories.VideoRepository;
import com.android.example.shortvideos.util.Result;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final VideoRepository videoRepository;

    private MutableLiveData<Result<List<MediaData>>> _mediaLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(@NonNull VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        getData();
    }

    public LiveData<Result<List<MediaData>>> getMediaLiveData() {
        return _mediaLiveData;
    }

    private void getData() {
        _mediaLiveData = videoRepository.getData();
    }
}

