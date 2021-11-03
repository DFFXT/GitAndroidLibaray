package com.example.activitycollection.base

import java.io.Serializable

/**
 * @author feiqin
 * @date 2021/10/19
 * @description activity 基本信息
 */
class ActivityInfo<T : Cloneable>(var hashCode: Int) : Serializable {
    var startTime: Long = -1
    var pauseTime: Long = -1
    var data: T? = null

    /**
     * 克隆对象
     */
    fun clone(): ActivityInfo<T> {
        val info = ActivityInfo<T>(hashCode)
        info.data = data
        info.startTime = -1
        info.pauseTime = -1
        return info
    }

    override fun toString(): String {
        return "startTime:" + startTime +" pauseTime:" + pauseTime + " data: " + data.toString()
    }
}