package com.turkcell.tech_assignment.bekarys.di.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.gson.Gson
import com.turkcell.tech_assignment.bekarys.di.viewmodel.ViewModelFactory
import com.turkcell.tech_assignment.bekarys.features.common.database.AppDatabase
import com.turkcell.tech_assignment.bekarys.features.common.local.LocalDirectories
import com.turkcell.tech_assignment.bekarys.features.common.network.DefaultNetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.network.NetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.network.RequestManager
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.threading.DefaultAppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule(
    private val application: Application
) {

    @Singleton
    @Provides
    fun provideContext(): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideRequestBuilder(): Request.Builder = Request.Builder()

    @Singleton
    @Provides
    fun provideNetworkStateManager(): NetworkStateManager = DefaultNetworkStateManager(application)

    @Singleton
    @Provides
    fun provideAppScheduler(): AppScheduler = DefaultAppScheduler()

    @Singleton
    @Provides
    fun provideRequestManager(
        client: OkHttpClient,
        requestBuilder: Request.Builder,
        gson: Gson,
        connectionManager: NetworkStateManager
    ): RequestManager = RequestManager(client, requestBuilder, gson, connectionManager)

    @Singleton
    @Provides
    fun provideViewModelMap(): ViewModelController = ViewModelController()

    @Singleton
    @Provides
    fun provideViewModelFactory(
        viewModelController: ViewModelController
    ): ViewModelProvider.Factory = ViewModelFactory(viewModelController)

    @Singleton
    @Provides
    fun provideLocalDirectories(context: Context): LocalDirectories = LocalDirectories(
        context.cacheDir.path
    )

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase_1_0_0"
        ).fallbackToDestructiveMigration()
            .build()

}