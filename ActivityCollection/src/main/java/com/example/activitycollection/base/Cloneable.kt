package com.example.activitycollection.base

import java.io.Serializable

/**
 * @author feiqin
 * @date 2021/10/8-19:38
 * @description 克隆对象
 */
interface Cloneable : Serializable, kotlin.Cloneable {
    /**
     * clone 对象
     */
    fun <T : Cloneable> copy(): T {
        return clone() as T
    }
}