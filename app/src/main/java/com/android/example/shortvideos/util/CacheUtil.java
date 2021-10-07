package com.android.example.shortvideos.util;


import android.content.Context;
import android.net.Uri;

import com.android.example.shortvideos.MyApplication;
import com.android.example.shortvideos.R;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheWriter;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;


public class CacheUtil {

    public static final SimpleCache simpleCache = MyApplication.simpleCache;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static synchronized void cacheVideos(ArrayList<String> urls, Context context) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < urls.size(); i++) {
                    Uri url = Uri.parse(urls.get(i));

                    DataSpec dataSpec = new DataSpec(url, 0, 300 * 1024);
                    DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                            context,
                            Util.getUserAgent(context, context.getString(R.string.app_name))
                    );

                    CacheDataSource dataSource = new CacheDataSource(simpleCache, defaultDataSourceFactory.createDataSource());

                    cacheVideo(dataSource, dataSpec);
                }
            }
        });
    }

    public static synchronized void cacheVideo(
            CacheDataSource cacheDataSource,
            DataSpec dataSpec
    ) {
        CacheWriter.ProgressListener listener = (requestLength, bytesCached, newBytesCached) -> {
            Double downloadProgress = (bytesCached * 100.0 / requestLength);

            Timber.d("Cache Progress %s %s", downloadProgress, dataSpec.uri.toString());
            Timber.d("Cached Space = %s", simpleCache.getCacheSpace());
        };

        executorService.execute(() -> {
            try {
                CacheWriter cacheWriter = new CacheWriter(
                        cacheDataSource,
                        dataSpec,
                        null,
                        listener
                );
                cacheWriter.cache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
