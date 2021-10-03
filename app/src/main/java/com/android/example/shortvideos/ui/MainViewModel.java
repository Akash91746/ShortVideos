package com.android.example.shortvideos.ui;

import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.repositories.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MutableLiveData<List<MediaData>> _liveData ;

    @Inject
    public MainViewModel(@NonNull VideoRepository videoRepository){
        _liveData = videoRepository.fetchData();
    }


    public LiveData<List<MediaData>> getLiveData() {
        return _liveData;
    }

}
