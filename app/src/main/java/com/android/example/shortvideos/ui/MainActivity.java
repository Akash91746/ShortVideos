package com.android.example.shortvideos.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.PagerSnapHelper;
import dagger.hilt.android.AndroidEntryPoint;

import android.os.Build;
import android.os.Bundle;

import com.android.example.shortvideos.adapters.VideoListAdapter;
import com.android.example.shortvideos.databinding.ActivityMainBinding;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding dataBinding;
    private MainViewModel mViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());

       mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        VideoListAdapter adapter = new VideoListAdapter(new VideoListAdapter.MediaDataDiffUtil());
        dataBinding.videoRecyclerView.setAdapter(adapter);

        final PagerSnapHelper linearSnapHelper = new PagerSnapHelper();
        linearSnapHelper.attachToRecyclerView(dataBinding.videoRecyclerView);

        mViewModel.getLiveData().observe(this, adapter::submitList);
    }

}