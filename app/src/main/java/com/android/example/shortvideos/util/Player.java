package com.android.example.shortvideos.util;

import android.content.Context;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import timber.log.Timber;

public class Player implements DefaultLifecycleObserver {

    private final Context context;
    private final PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    public Player(Context context, PlayerView playerView, ConcatenatingMediaSource mediaSource) {
        this.context = context;
        this.playerView = playerView;

        simpleExoPlayer = new SimpleExoPlayer.Builder(context)
                .setUseLazyPreparation(true)
                .build();
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setMediaSource(mediaSource);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setRepeatMode(com.google.android.exoplayer2.Player.REPEAT_MODE_ALL);
    }

    public SimpleExoPlayer getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    void resumePlayer() {
        simpleExoPlayer.play();
    }

    void pausePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.pause();
        }
    }

    void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.pause();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        resumePlayer();
        Timber.d("Calling resume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        pausePlayer();
        Timber.d("Calling pause");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        releasePlayer();
        Timber.d("Calling destroy");
    }

//    @Override
//    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
//        if (source == context){
//            Timber.d("State Changes occur");
//            if (event == Lifecycle.Event.ON_RESUME){
//                resumePlayer();
//            }else if (event == Lifecycle.Event.ON_PAUSE){
//                pausePlayer();
//            }else if (event == Lifecycle.Event.ON_DESTROY){
//                releasePlayer();
//            }
//        }
//    }
}
