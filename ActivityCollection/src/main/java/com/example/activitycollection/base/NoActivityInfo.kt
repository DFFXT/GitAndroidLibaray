package com.iflytek.aistudyclient.english.lib.learn.report.base

/**
 * @author feiqin
 * @date 2021/10/20-10:18
 * @description 空接口，仅标志当前activity的数据为无效数据（数据仍存储在intent）
 * 实现了该接口的activity在onPause时，不会触发上报
 */
interface NoActivityInfo