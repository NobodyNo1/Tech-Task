package com.turkcell.tech_assignment.bekarys.di.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.turkcell.tech_assignment.bekarys.application.TestApplication
import com.turkcell.tech_assignment.bekarys.di.viewmodel.ViewModelFactory
import com.turkcell.tech_assignment.bekarys.di.viewmodel.ViewModelModule
import com.turkcell.tech_assignment.bekarys.features.common.database.AppDatabase
import com.turkcell.tech_assignment.bekarys.features.common.local.LocalDirectories
import com.turkcell.tech_assignment.bekarys.features.common.network.NetworkStateManager
import com.turkcell.tech_assignment.bekarys.features.common.network.RequestManager
import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import com.turkcell.tech_assignment.bekarys.features.common.viewmodel.ViewModelController
import dagger.Component
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Component(
    modules = [
        AppModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(application: TestApplication)

    fun okHttpClient(): OkHttpClient

    fun gson(): Gson

    fun requestBuilder(): Request.Builder

    fun context(): Context

    fun networkStateManager(): NetworkStateManager

    fun appScheduler(): AppScheduler

    fun viewModelController(): ViewModelController

    fun viewModelFactory():  ViewModelProvider.Factory

    fun requestManager(): RequestManager

    fun localDirectories(): LocalDirectories

    fun appDatabase(): AppDatabase
}
