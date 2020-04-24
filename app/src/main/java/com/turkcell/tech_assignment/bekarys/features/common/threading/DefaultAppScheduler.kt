package com.turkcell.tech_assignment.bekarys.features.common.threading

import com.turkcell.tech_assignment.bekarys.features.common.threading.AppScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler

class DefaultAppScheduler :
    AppScheduler {

    override fun getIOScheduler(): Scheduler = Schedulers.io()

    override fun getUIScheduler(): Scheduler = AndroidSchedulers.mainThread()

}