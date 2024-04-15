package com.ksnk.image.di.module

import com.google.gson.Gson
import com.ksnk.image.remote.Api
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideHttpClient(): OkHttpClient =
    OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

fun provideConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://ksnk-dm.hqsite.online/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

fun provideService(retrofit: Retrofit): Api =
    retrofit.create(Api::class.java)

val networkModule = module {
    single { provideHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get(), get()) }
    single { provideService(get()) }
    single { Gson() }
}