package com.teovladusic.core.data.di

import com.teovladusic.core.data.repository.FriendRepositoryImpl
import com.teovladusic.core.data.retrofit.RetrofitNetworkUserApi
import com.teovladusic.core.domain.repository.FriendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFriendRepository(api: RetrofitNetworkUserApi): FriendRepository =
        FriendRepositoryImpl(api)
}