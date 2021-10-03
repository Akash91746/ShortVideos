package com.android.example.shortvideos;

import android.app.Application;

import com.android.example.shortvideos.util.Constants;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class MyApplication extends Application {

    public static SimpleCache simpleCache;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        final LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor =
                new LeastRecentlyUsedCacheEvictor(Constants.EXO_PLAYER_CACHE_SIZE);

        final ExoDatabaseProvider databaseProvider = new ExoDatabaseProvider(this);

        final File cacheDir = new File(this.getCacheDir(), "media");
        simpleCache = new SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, databaseProvider);
    }
}
