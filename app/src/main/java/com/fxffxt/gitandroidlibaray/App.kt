package com.fxffxt.gitandroidlibaray

import android.app.Application
import android.content.Context
import com.example.activitycollection.StackInstaller
import com.example.activitycollection.base.ActivityInfo
import com.example.activitycollection.base.ActivityStackCollection
import com.example.activitycollection.bean.Module

/**
 * @author feiqin
 * @date 2021/10/19
 * @description application
 */
class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        StackInstaller.install(
            this,
            object : ActivityStackCollection<Module>() {
                override fun onPauseWithInfo(info: ActivityInfo<Module>) {
                }
            }
        )
    }
    override fun onCreate() {
        ctx = this
        super.onCreate()
    }
    companion object {
        @JvmStatic
        lateinit var ctx: Application
    }
}