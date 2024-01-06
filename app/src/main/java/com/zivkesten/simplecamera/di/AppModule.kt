package com.zivkesten.simplecamera.di

import android.app.Application
import android.content.Context
import com.zivkesten.simplecamera.usecases.ImageCaptureUseCase
import com.zivkesten.simplecamera.usecases.ImageCaptureUseCaseImpl
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
    ): ImageCaptureUseCase = ImageCaptureUseCaseImpl(context)
}
