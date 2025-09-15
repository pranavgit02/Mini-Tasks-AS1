package com.example.streamchatdemo.di

import com.example.streamchatdemo.service.FakeGreetingService
import com.example.streamchatdemo.service.GreetingService
import com.example.streamchatdemo.service.RealGreetingService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    @Real
    abstract fun bindRealGreetingService(impl: RealGreetingService): GreetingService

    @Binds
    @Singleton
    @Fake
    abstract fun bindFakeGreetingService(impl: FakeGreetingService): GreetingService
}
