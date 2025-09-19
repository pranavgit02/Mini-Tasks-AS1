package com.example.streamchatdemo.di

import com.example.coreengine.FakeEngine
import com.example.coreengine.ModelEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {

    @Provides
    @Singleton
    fun provideModelEngine(): ModelEngine = FakeEngine()
}
