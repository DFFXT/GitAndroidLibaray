package com.example.activitycollection.base

/**
 * @author feiqin
 * @date 2021/10/26-15:07
 * @description
 */
interface IReport<T : Cloneable> {
    /**
     * get report info
     */
    fun getReportInfo(): T
}