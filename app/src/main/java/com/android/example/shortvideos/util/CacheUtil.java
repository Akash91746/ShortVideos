package com.android.example.shortvideos.util;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.example.shortvideos.MyApplication;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheWriter;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

//The util class for caching videos

public class CacheUtil {

    private static CacheUtil INSTANCE = null;

    private CacheDataSource cacheDataSourceFactory;
    private final ExecutorService executorService;
    private final SimpleCache simpleCache;

    private CacheUtil() {
        executorService = Executors.newFixedThreadPool(5);
        simpleCache = MyApplication.simpleCache;
    }

    public void preCacheVideo(Uri url, Context context) {

        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true);

        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                context, httpDataSourceFactory
        );

        cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .createDataSource();

        DataSpec dataSpec = new DataSpec(url,0,60*60*10);

        CacheWriter.ProgressListener listener = (requestLength, bytesCached, newBytesCached) -> {
            Double downloadProgress = (bytesCached * 100.0 / requestLength);

            Timber.d("Cache Progress %s %s", downloadProgress,url.toString());
        };

        cacheVideo(dataSpec, listener);
    }

    private void cacheVideo(
            DataSpec dataSpec,
            CacheWriter.ProgressListener progressListener
    ) {
        executorService.execute(() -> {
            try {
                CacheWriter cacheWriter = new CacheWriter(
                        cacheDataSourceFactory,
                        dataSpec,
                        null,
                        progressListener
                );
                cacheWriter.cache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static CacheUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheUtil();
        }
        return INSTANCE;
    }

}

