package com.zivkesten.simplecamera.di

import android.app.Application
import android.content.Context
import androidx.camera.core.ImageCapture
import com.zivkesten.simplecamera.usecases.GetTakenPhotoUseCase
import com.zivkesten.simplecamera.usecases.GetTakenPhotoUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application
    @Singleton
    @Provides
    fun provideTakePhotoUseCase(
        context: Context,
    ): GetTakenPhotoUseCase = GetTakenPhotoUseCaseImpl(context)
}
