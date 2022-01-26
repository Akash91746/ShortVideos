package com.android.example.shortvideos.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.android.example.shortvideos.adapters.VideoListAdapter;
import com.android.example.shortvideos.databinding.ActivityMainBinding;
import com.android.example.shortvideos.models.MediaData;
import com.android.example.shortvideos.util.Result;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding dataBinding;
    private MainViewModel mViewModel;
    private VideoListAdapter adapter;
    private VideoListAdapter.ViewHolder viewHolder;

    public static boolean checkNetworkStat(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        adapter = new VideoListAdapter(new VideoListAdapter.MediaDataDiffUtil(), this);
        dataBinding.videoRecyclerView.setAdapter(adapter);

        checkNetworkStatusAndLoadData();
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    public void checkNetworkStatusAndLoadData() {
        if (!checkNetworkStat(getApplicationContext())) {
            View view = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, "Network Error!", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("RETRY", v -> checkNetworkStatusAndLoadData());
            snackbar.show();
        } else {
            mViewModel.getMediaLiveData().observe(this, new Observer<Result<List<MediaData>>>() {
                @Override
                public void onChanged(Result<List<MediaData>> listResult) {

                    if (listResult instanceof Result.Success) {
                        adapter.submitList(((Result.Success<List<MediaData>>) listResult).data);
                    }
                    if (listResult instanceof Result.Error) {
                        String error = ((Result.Error<List<MediaData>>) listResult).exception;

                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewHolder != null) {
            viewHolder.initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LinearLayoutManager layoutManager = (LinearLayoutManager) dataBinding.videoRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            int pos = layoutManager.findFirstVisibleItemPosition();
            viewHolder = (VideoListAdapter.ViewHolder) dataBinding.videoRecyclerView.findViewHolderForAdapterPosition(pos);
            if (viewHolder != null) {
                viewHolder.releasePlayer();
            }
        }
    }
}