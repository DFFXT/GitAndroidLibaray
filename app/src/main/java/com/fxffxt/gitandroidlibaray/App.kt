package com.fxffxt.gitandroidlibaray

import android.app.Application
import android.content.Context

class App: Application() {
    override fun onCreate() {
        ctx = applicationContext
        super.onCreate()
    }
    companion object{
        @JvmStatic
        lateinit var ctx: Context
    }
}