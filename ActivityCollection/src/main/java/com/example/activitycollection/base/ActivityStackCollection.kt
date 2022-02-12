package com.example.activitycollection.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import com.example.activitycollection.ext.ACTIVITY_INFO_KEY
import com.example.activitycollection.ext.getActivityInfo
import com.example.activitycollection.ext.getActivityInfoData
import com.example.activitycollection.ext.setActivityInfo
import com.example.activitycollection.ext.setActivityInfoData
import com.iflytek.aistudyclient.english.lib.learn.report.base.NoActivityInfo
import com.iflytek.aistudyclient.english.lib.learn.report.ext.log

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
 * @see com.iflytek.aistudyclient.english.lib.learn.report.DefaultActivityStackCollection
 */
abstract class ActivityStackCollection<T : Cloneable> : Application.ActivityLifecycleCallbacks, IActivityStart {
    private val tag = "ActivityStackCollection"
    // activity 信息列表，用于信息继承

    // private var infoCache: ActivityInfo<T>? = null

    override fun onActivityStart(who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?, intent: Intent, requestCode: Int, options: Bundle?) {
        if (intent.hasExtra(ACTIVITY_INFO_KEY)) {
            throw Exception("ActivityStackCollection: intent 中${ACTIVITY_INFO_KEY}被占用")
        }
        // 启动activity的回调，将前一个activity的信息传递到下一个页面
        val data = target?.getActivityInfoData<T>()
        if (data != null) {
            intent.setActivityInfo(
                ActivityInfo<T>().apply {
                    this.data = data
                    this.startTime = SystemClock.elapsedRealtime()
                }
            )
        }
    }

    override fun onNewIntent(activity: Activity, intent: Intent) {
        // 将新Intent中的模块信息更新到旧的Intent中，防止使用者没有重写onNewIntent
        val data = intent.getActivityInfo<T>()?.data
        if (data != null) {
            activity.setActivityInfoData(data)
        }
    }

    // activity创建/重建
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log(tag, "create:" + activity::class.java.simpleName)
        // 信息来源优先级：接口获取 > bundle存储 > 信息继承
        var infoCache: ActivityInfo<T>? = null
        if (activity is IReport<*>) {
            infoCache = ActivityInfo()
            // 这里需要注意接口返回的类型
            infoCache.data = activity.getReportInfo() as? T
        } else {
            val bundleInfo = savedInstanceState?.getSerializable(ACTIVITY_INFO_KEY) as? ActivityInfo<T>
            if (bundleInfo != null) {
                infoCache = bundleInfo
            } else {
                if (infoCache != null) {
                    // 通过继承前一个获取信息
                    infoCache.clone().let {
                        infoCache = it
                    }
                } else {
                    // 从intent中取数据
                    infoCache = activity.getActivityInfo()
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
        activity.getActivityInfo<T>()?.let {
            it.startTime = SystemClock.elapsedRealtime()
            it.pauseTime = it.startTime
            // 重新赋值
            activity.setActivityInfo(it)
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
            onPauseWithInfo(info.copy())
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

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * 触发一次上报
     */
    fun makeReport(info: ActivityInfo<*>?) {
        info ?: return
        // 赋值pauseTime
        info.pauseTime = SystemClock.elapsedRealtime()
        onPauseWithInfo(info.copy() as ActivityInfo<T>)
        // 在上报触发完成之后，将startTime重新计时
        info.startTime = info.pauseTime
        log(tag, " makeReport info:" + info.data.toString())
    }

    /**
     * 有info的activity进行了暂停
     */
    abstract fun onPauseWithInfo(info: ActivityInfo<T>)
}