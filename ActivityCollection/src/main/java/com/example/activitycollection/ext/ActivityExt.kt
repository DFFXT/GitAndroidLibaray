package com.example.activitycollection.ext

import android.app.Activity
import android.content.Intent
import com.example.activitycollection.base.ActivityInfo
import com.example.activitycollection.base.Cloneable
import com.iflytek.aistudyclient.english.lib.learn.report.ext.log

/**
 * @author feiqin
 * @date 2021/10/20-9:29
 * @description
 */
const val ACTIVITY_INFO_KEY = "_ActivityStackCollection_"

/**
 * intent获取activity信息
 */
fun <T : Cloneable> Activity.getActivityInfo(): ActivityInfo<T>? {
    return intent?.getActivityInfo()
}

/**
 * 获取activity info中的data
 */
fun <T : Cloneable> Activity.getActivityInfoData(): T? {
    return getActivityInfo<T>()?.data
}

/**
 * 获取Intent中的页面信息
 */
fun <T : Cloneable> Intent.getActivityInfo(): ActivityInfo<T>? {
    return getSerializableExtra(ACTIVITY_INFO_KEY) as? ActivityInfo<T>
}

/**
 * 设置页面信息到Intent
 */
fun <T : Cloneable> Intent.setActivityInfo(info: ActivityInfo<T>?) {
    putExtra(ACTIVITY_INFO_KEY, info)
}

/**
 * 设置activity信息到intent
 */
fun <T : Cloneable> Activity.setActivityInfo(activityInfo: ActivityInfo<T>?) {
    intent?.setActivityInfo(activityInfo)
}

/**
 * 保存infoData
 */
fun <T : Cloneable> Activity.setActivityInfoData(infoData: T?) {
    val info = getActivityInfo<T>()
    info?.let {
        info.data = infoData
        setActivityInfo(it)
    } ?: log("ActivityExt", "保存 infoData 失败，无页面信息")
}

/**
 * 移除intent信息
 */
fun Activity.removeActivityInfo() {
    intent?.removeExtra(ACTIVITY_INFO_KEY)
}