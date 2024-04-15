package com.ksnk.image.di.module

import com.ksnk.image.remote.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {
    factory {  Repository(get()) }
}