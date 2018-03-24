package com.stedi.randomimagegenerator.app.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class AppContext

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class DefaultScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RigScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class UiScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RootSavePath
