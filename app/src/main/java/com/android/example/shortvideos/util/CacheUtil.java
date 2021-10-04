package com.android.example.shortvideos.util;


import android.content.Context;
import android.net.Uri;

import com.android.example.shortvideos.MyApplication;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheWriter;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;


public class CacheUtil {

    private static CacheUtil INSTANCE = null;
    private final ExecutorService executorService;
    private final SimpleCache simpleCache;
    private CacheDataSource cacheDataSourceFactory;
    private DefaultDataSourceFactory defaultDataSourceFactory;
    private HttpDataSource.Factory httpDataSourceFactory;

    private CacheUtil() {
        executorService = Executors.newFixedThreadPool(5);
        simpleCache = MyApplication.simpleCache;
    }

    public static CacheUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheUtil();
        }
        return INSTANCE;
    }

    public synchronized void preCacheVideo(Uri url, Context context) {


        httpDataSourceFactory = new DefaultHttpDataSource.Factory();

        cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(simpleCache)
                .setUpstreamDataSourceFactory(httpDataSourceFactory)
                .createDataSource();

        DataSpec dataSpec = new DataSpec(url, 0, 36000);

        CacheWriter.ProgressListener listener = (requestLength, bytesCached, newBytesCached) -> {
            Double downloadProgress = (bytesCached * 100.0 / requestLength);

            Timber.d("Cache Progress %s %s", downloadProgress, url.toString());
        };

        cacheVideo(cacheDataSourceFactory, dataSpec, listener);
    }

    public synchronized void cacheVideo(
            CacheDataSource cacheDataSource,
            DataSpec dataSpec,
            CacheWriter.ProgressListener progressListener
    ) {
        executorService.execute(() -> {
            try {
                CacheWriter cacheWriter = new CacheWriter(
                        cacheDataSource,
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

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public CacheDataSource getCacheDataSourceFactory() {
        return cacheDataSourceFactory;
    }

    public DefaultDataSourceFactory getDefaultDataSourceFactory() {
        return defaultDataSourceFactory;
    }

    public HttpDataSource.Factory getHttpDataSourceFactory() {
        if (httpDataSourceFactory == null) {
            return new DefaultHttpDataSource.Factory();
        }
        return httpDataSourceFactory;
    }

    public SimpleCache getSimpleCache() {
        return this.simpleCache;
    }

}
