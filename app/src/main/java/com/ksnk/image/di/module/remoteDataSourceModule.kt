package com.ksnk.image.di.module

import com.ksnk.image.remote.RemoteDataSource
import org.koin.dsl.module

val remoteDataSourceModule= module {
    factory {  RemoteDataSource(get()) }
}