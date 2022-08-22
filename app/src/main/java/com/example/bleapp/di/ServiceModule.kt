package com.example.bleapp.di

import android.content.Context
import com.example.bleapp.services.ScannerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
object ServiceModule {

    @ActivityRetainedScoped
    @Provides
    fun providesScannerService(@ApplicationContext context: Context): ScannerService {
        return ScannerService(context)
    }
}