package com.example.activitycollection.base

import java.io.Serializable

/**
 * @author feiqin
 * @date 2021/10/19
 * @description activity 基本信息
 */
class ActivityInfo<T : Cloneable>() : Serializable {
    var startTime: Long = -1
    var pauseTime: Long = -1
    var data: T? = null

    /**
     * 克隆对象
     */
    fun clone(): ActivityInfo<T> {
        val info = ActivityInfo<T>()
        info.data = data?.copy()
        info.startTime = -1
        info.pauseTime = -1
        return info
    }

    /**
     * 完全复制一份对象，所有属性一致
     */
    internal fun copy(): ActivityInfo<T> {
        val info = ActivityInfo<T>()
        info.data = data
        info.startTime = startTime
        info.pauseTime = pauseTime
        return info
    }
}