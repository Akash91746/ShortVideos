package com.android.example.shortvideos;

import com.android.example.shortvideos.repositories.VideoRepository;
import com.android.example.shortvideos.repositories.VideosApi;
import com.android.example.shortvideos.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
final class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static VideosApi providesVideosApi() {
        return provideRetrofitInstance().create(VideosApi.class);
    }

    @Singleton
    @Provides
    static VideoRepository providesVideoRepository() {
        return new VideoRepository(providesVideosApi());
    }


}
