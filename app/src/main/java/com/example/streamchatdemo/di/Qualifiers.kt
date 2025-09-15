package com.example.streamchatdemo.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Real

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Fake
