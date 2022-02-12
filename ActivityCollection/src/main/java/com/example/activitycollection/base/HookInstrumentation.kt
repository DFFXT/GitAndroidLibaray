package com.example.activitycollection.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder

/**
 * @author feiqin
 * @date 2022/2/12-11:29
 * @description hook Activity的启动
 */
class HookInstrumentation(private val origin: Instrumentation, private val iActivityStart: IActivityStart) : Instrumentation() {
    /**
     * execStartActivity
     * 没有override关键字，但是仍然是重写了的
     */
    fun execStartActivity(
        who: Context?,
        contextThread: IBinder?,
        token: IBinder?,
        target: Activity?,
        intent: Intent,
        requestCode: Int,
        options: Bundle?
    ): ActivityResult? {
        try {
            val method = Instrumentation::class.java.getMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                Activity::class.java, Intent::class.java, Int::class.java, Bundle::class.java
            )
            method.isAccessible = true
            iActivityStart.onActivityStart(who, contextThread, token, target, intent, requestCode, options)
            return method.invoke(origin, who, contextThread, token, target, intent, requestCode, options) as? ActivityResult
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun callActivityOnNewIntent(activity: Activity, intent: Intent) {
        iActivityStart.onNewIntent(activity, intent)
        super.callActivityOnNewIntent(activity, intent)
    }
}