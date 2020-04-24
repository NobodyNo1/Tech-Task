package com.turkcell.tech_assignment.bekarys.features.common.threading

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestAppScheduler(private val sheduler: Scheduler = Schedulers.trampoline()) : AppScheduler {
    override fun getIOScheduler(): Scheduler = sheduler
    override fun getUIScheduler(): Scheduler = sheduler
}