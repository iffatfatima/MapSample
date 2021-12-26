package com.app.mapsample.di

import com.app.mapsample.domain.repository.LocationRepository
import com.app.mapsample.data.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideLiveLocationRepo(liveLocationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}
