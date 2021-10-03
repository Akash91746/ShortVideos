package com.android.example.shortvideos;

import android.content.Context;

import com.android.example.shortvideos.repositories.VideoRepository;
import com.android.example.shortvideos.repositories.VideosApi;
import com.android.example.shortvideos.util.Constants;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
final class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(){

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static VideosApi providesVideosApi(){
        return provideRetrofitInstance().create(VideosApi.class);
    }

    @Singleton
    @Provides
    static VideoRepository providesVideoRepository(){
        return new VideoRepository(providesVideosApi());
    }


}
