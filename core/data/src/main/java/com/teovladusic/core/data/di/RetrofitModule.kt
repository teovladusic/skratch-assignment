package com.teovladusic.core.data.di

import com.google.gson.GsonBuilder
import com.teovladusic.core.data.BuildConfig
import com.teovladusic.core.data.network.ApiResultAdapterFactory
import com.teovladusic.core.data.retrofit.RetrofitNetworkUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {
    private const val READ_TIMEOUT_SECONDS = 60L
    private const val WRITE_TIMEOUT_SECONDS = 120L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        clientBuilder.readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        GsonConverterFactory.create(GsonBuilder().serializeNulls().create())

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResultAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofitNetworkUserApi(retrofit: Retrofit): RetrofitNetworkUserApi =
        retrofit.create(RetrofitNetworkUserApi::class.java)
}