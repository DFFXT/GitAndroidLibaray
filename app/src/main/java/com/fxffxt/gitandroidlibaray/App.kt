package com.fxffxt.gitandroidlibaray

import android.app.Application
import com.example.activitycollection.base.ActivityInfo
import com.example.activitycollection.base.ActivityStackCollection
import com.example.activitycollection.bean.Module

/**
 * @author feiqin
 * @date 2021/10/19
 * @description application
 */
class App : Application() {
    override fun onCreate() {
        ctx = this
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityStackCollection<Module>() {
            override fun onPauseWithInfo(info: ActivityInfo<Module>) {
            }
        })
    }
    companion object {
        @JvmStatic
        lateinit var ctx: Application
    }
}