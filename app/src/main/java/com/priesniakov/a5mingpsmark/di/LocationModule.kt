package com.priesniakov.a5mingpsmark.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.priesniakov.a5mingpsmark.BuildConfig
import com.priesniakov.a5mingpsmark.domain.LocationUseCase
import com.priesniakov.a5mingpsmark.domain.LocationUseCaseImpl
import com.priesniakov.a5mingpsmark.domain.api.DefaultInterceptor
import com.priesniakov.a5mingpsmark.domain.api.LocationApi
import com.priesniakov.a5mingpsmark.domain.datasource.LocationDatasource
import com.priesniakov.a5mingpsmark.domain.datasource.LocationDatasourceImpl
import com.priesniakov.a5mingpsmark.domain.repository.LocationRepository
import com.priesniakov.a5mingpsmark.domain.repository.LocationRepositoryImpl
import com.priesniakov.a5mingpsmark.location.LocationAlarm
import com.priesniakov.a5mingpsmark.location.LocationAlarmImpl
import com.priesniakov.a5mingpsmark.location.LocationFacade
import com.priesniakov.a5mingpsmark.location.LocationFacadeImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationBindingModule {

    @Binds
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindLocationDatasource(datasource: LocationDatasourceImpl): LocationDatasource

    @Binds
    abstract fun bindLocationUseCase(locationUseCase: LocationUseCaseImpl): LocationUseCase

    @Binds
    abstract fun bindLocationsFacade(locationFacade: LocationFacadeImpl): LocationFacade

    @Binds
    abstract fun bindLocationAlarm(locationAlarm: LocationAlarmImpl): LocationAlarm
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()


    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor(DefaultInterceptor())
        return builder.build()
    }

    @Provides
    fun provideLocationApi(retrofit: Retrofit) =
        retrofit.create(LocationApi::class.java)

    private const val TIMEOUT_SEC: Long = 10
}