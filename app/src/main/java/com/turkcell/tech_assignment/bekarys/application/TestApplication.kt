package com.turkcell.tech_assignment.bekarys.application

import android.app.Application
import com.turkcell.tech_assignment.bekarys.di.app.AppModule
import com.turkcell.tech_assignment.bekarys.di.app.DaggerAppComponent

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupDI()
    }

    private fun setupDI() {
        val component = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
        component.inject(this)

        ApplicationComponentHolder.component = component
    }
}