package com.github.bccatest

import android.app.Application
import android.content.Context
import com.github.bccatest.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class AlbumApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: AlbumApplication
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlbumApplication)
            //module list
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    localDataModule,
                    useCaseModule,
                    apiModule
                )
            )
        }
    }

}
