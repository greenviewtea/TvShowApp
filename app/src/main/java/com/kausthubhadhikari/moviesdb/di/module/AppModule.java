package com.kausthubhadhikari.moviesdb.di.module;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kausthubhadhikari.moviesdb.AppController;
import com.kausthubhadhikari.moviesdb.R;
import com.kausthubhadhikari.moviesdb.model.api.APIService;
import com.kausthubhadhikari.moviesdb.model.manager.NetworkManager;
import com.kausthubhadhikari.moviesdb.utils.misc.AppConstants;
import com.kausthubhadhikari.moviesdb.utils.misc.AppUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kausthubhadhikari on 12/12/16.
 */
@Module
public class AppModule {

    private final AppController appController;

    public AppModule(AppController appController) {
        this.appController = appController;
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public APIService providesApiService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }

    @Provides
    @Singleton
    public AppUtils providesAppUtils() {
        return new AppUtils();
    }

    @Provides
    @Singleton
    public MaterialDialog providesMaterialDialogLoader() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(appController)
                .customView(R.layout.progress_dialog, false)
                .cancelable(false)
                .theme(Theme.LIGHT)
                .build();
        materialDialog.getWindow().setLayout(85, 85);
        materialDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));

        return materialDialog;
    }

    @Provides
    @Singleton
    public NetworkManager providesNetworkManager(APIService apiService) {
        return new NetworkManager(apiService);
    }
}