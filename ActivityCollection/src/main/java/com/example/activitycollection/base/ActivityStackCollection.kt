package com.example.activitycollection.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.iflytek.aistudyclient.english.lib.learn.report.base.NoActivityInfo
import com.iflytek.aistudyclient.english.lib.learn.report.ext.ACTIVITY_INFO_KEY
import com.iflytek.aistudyclient.english.lib.learn.report.ext.getActivityInfo
import com.iflytek.aistudyclient.english.lib.learn.report.ext.setActivityInfo

/**
 * @author feiqin
 * @date 2021/9/28-15:46
 * @description activity 基本信息
 * @type T 页面信息bean，能够通过clone进行复制
 * ·初次进入的activity必须能够通过name获取对应的信息
 * ·后续的activity优先通过name获取信息，无法获取信息就继承上一个activity的信息
 * ·activity的信息通过hashCode存储
 * ·activity的信息需要在onCreate中存储到intent中
 * ·信息来源优先级：IReport获取 > bundle存储 > 信息继承 > 兜底信息（只有时长）
 * .如果存在singleInstance，需要在onNewIntent中清除bundle存储的页面数据
 * ·done
 */
abstract class ActivityStackCollection<T : Cloneable> : Application.ActivityLifecycleCallbacks {
    private val tag = "ActivityStackCollection"
    // activity 信息列表，用于信息继承

    private var infoCache: ActivityInfo<T>? = null

    // activity创建/重建
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log(tag, "create:" + activity::class.java.simpleName)
        // 信息来源优先级：接口获取 > bundle存储 > 信息继承
        if (activity is IReport<*>) {
            infoCache = ActivityInfo(activity.hashCode())
            // 这里需要注意接口返回的类型
            infoCache?.data = activity.getReportInfo() as T
        } else {
            val bundleInfo = savedInstanceState?.getSerializable(ACTIVITY_INFO_KEY) as? ActivityInfo<T>
            if (bundleInfo != null) {
                // 通过bundle存储的信息，一般重建activity用到，需要重新赋值hashCode
                bundleInfo.hashCode = activity.hashCode()
            } else {
                if (infoCache != null) {
                    // 通过继承前一个获取信息
                    infoCache?.clone()?.let {
                        it.hashCode = activity.hashCode()
                        infoCache = it
                    }
                } else {
                    // 没有前一个信息，只记录时长，一般再单模块app里面（兜底信息）
                    infoCache = ActivityInfo(activity.hashCode())
                }
            }
        }
        infoCache?.let {
            activity.setActivityInfo(it)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        log(tag, "start:" + activity::class.java.simpleName)
    }

    override fun onActivityResumed(activity: Activity) {
        log(tag, "resume:" + activity::class.java.simpleName)
        val info = activity.getActivityInfo<T>()
        // activity信息为null，同时该activity没有标志NoActivityInfo接口，说明信息丢失或者手动去除了。此时需要从上个activity继承
        if (info == null) {
            // 需要先移除列表中activity对应的无效信息
            infoCache?.clone()?.let {
                it.hashCode = activity.hashCode()
            }
        }
        info?.let {
            it.startTime = SystemClock.elapsedRealtime()
            it.pauseTime = it.startTime
            activity.setActivityInfo(it)
            // 将info移到最后
            infoCache = info
        }
    }

    override fun onActivityPaused(activity: Activity) {
        log(tag, "pause:" + activity::class.java.simpleName)
        val info = activity.getActivityInfo<T>()
        if (info != null) {
            info.pauseTime = SystemClock.elapsedRealtime()
            activity.setActivityInfo(info)
            // 不触发onPauseWithInfo
            if (activity is NoActivityInfo) return
            // pause 上报时机
            onPauseWithInfo(info)
            log(tag, activity::class.java.simpleName + " info:" + info.data.toString())
        }
    }

    override fun onActivityStopped(activity: Activity) {
        log(tag, "stop:" + activity::class.java.simpleName)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val info = activity.getActivityInfo<T>()
        if (info != null) {
            // 防止activity销毁
            outState.putSerializable(ACTIVITY_INFO_KEY, info)
        }
        log(tag, "save:" + activity::class.java.simpleName)
    }

    override fun onActivityDestroyed(activity: Activity) {
        log(tag, "destroyed:" + activity::class.java.simpleName)
        if (activity.hashCode() == infoCache?.hashCode) {
            infoCache = null
        }
    }

    /**
     * 有info的activity进行了暂停
     */
    abstract fun onPauseWithInfo(info: ActivityInfo<T>)

    private fun log(tag: String, msg: String) {
        Log.i(tag, msg)
    }
}