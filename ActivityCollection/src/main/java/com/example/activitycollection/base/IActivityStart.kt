package com.example.activitycollection.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

/**
 * @author feiqin
 * @date 2022/2/12-14:46
 * @description
 */
interface IActivityStart {
    /**
     * onActivityStart
     */
    fun onActivityStart(who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?, intent: Intent, requestCode: Int, options: Bundle?)
    /**
     * onNewIntent
     */
    fun onNewIntent(activity: Activity, intent: Intent)
}