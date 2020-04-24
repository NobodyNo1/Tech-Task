package com.turkcell.tech_assignment.bekarys.features.common.threading

import io.reactivex.Scheduler

interface AppScheduler {

    fun getIOScheduler(): Scheduler

    fun getUIScheduler(): Scheduler
}