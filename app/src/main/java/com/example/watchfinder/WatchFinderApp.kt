package com.example.watchfinder

import android.app.Application

import coil3.ImageLoader
import coil3.SingletonImageLoader 
import coil3.PlatformContext 

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp

class WatchFinderApp : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var imageLoaderProvider: Provider<ImageLoader> 

    override fun onCreate() {
        super.onCreate()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return imageLoaderProvider.get()
    }
}