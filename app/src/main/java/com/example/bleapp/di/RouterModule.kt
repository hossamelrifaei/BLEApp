package com.example.bleapp.di

import com.example.bleapp.ui.Router
import com.example.bleapp.ui.RouterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
object RouterModule {

    @ActivityRetainedScoped
    @Provides
    fun providesRouter(): Router {
        return RouterImpl()
    }
}