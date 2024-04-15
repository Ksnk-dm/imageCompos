package com.ksnk.image

import android.app.Application
import com.ksnk.image.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class ImageApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ImageApplication)
            androidLogger()
            modules(
                viewModelModule
            )
        }
    }
}