package com.iflytek.aistudyclient.english.lib.learn.report.ext

/**
 * @author feiqin
 * @date 2021/10/26-19:01
 * @description string ext
 */
/**
 * 当str不为空时调用
 */
fun String?.notNullOrEmpty(call: String.() -> Unit) {
    if (!isNullOrEmpty()) {
        call(this)
    }
}