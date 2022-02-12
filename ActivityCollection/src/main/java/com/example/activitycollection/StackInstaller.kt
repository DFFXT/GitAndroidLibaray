package com.example.activitycollection

import android.app.Application
import android.app.Instrumentation
import com.example.activitycollection.base.ActivityStackCollection
import com.example.activitycollection.base.HookInstrumentation
import java.lang.Exception

/**
 * @author feiqin
 * @date 2022/2/12-14:34
 * @description
 */
class StackInstaller {
    companion object {
        /**
         * 安装页面信息统计插件
         */
        fun install(app: Application, activityStackCollection: ActivityStackCollection<*>) {
            // hook activity的启动
            try {
                val cls = Class.forName("android.app.ActivityThread")
                val field = cls.getDeclaredField("mInstrumentation").apply {
                    isAccessible = true
                }
                val activityThread = cls.getDeclaredField("sCurrentActivityThread").run {
                    isAccessible = true
                    get(null)
                }
                field.set(activityThread, HookInstrumentation(field.get(activityThread) as Instrumentation, activityStackCollection))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            app.registerActivityLifecycleCallbacks(activityStackCollection)
        }
    }
}