package com.fxffxt.hook

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * @author feiqin
 * @date 2022/2/12-9:46
 * @description
 */
class MyInstrumentation(private val origin: Instrumentation) : Instrumentation() {
    /**
     * execStartActivity
     */
    fun execStartActivity(
        who: Context?,
        contextThread: IBinder?,
        token: IBinder?,
        target: Activity?,
        intent: Intent?,
        requestCode: Int,
        options: Bundle?
    ): ActivityResult? {
        val method = Instrumentation::class.java.getMethod(
            "execStartActivity",
            Context::class.java,
            IBinder::class.java,
            IBinder::class.java,
            Activity::class.java, Intent::class.java, Int::class.java, Bundle::class.java
        )
        method.isAccessible = true
        return method.invoke(origin, who, contextThread, token, target, intent, requestCode, options) as? ActivityResult
    }

    override fun callActivityOnNewIntent(activity: Activity?, intent: Intent?) {
        super.callActivityOnNewIntent(activity, intent)
    }

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        return super.newActivity(cl, className, intent)
        Log.i("FFFX","sffsd2")
    }

    override fun newActivity(
        clazz: Class<*>?,
        context: Context?,
        token: IBinder?,
        application: Application?,
        intent: Intent?,
        info: ActivityInfo?,
        title: CharSequence?,
        parent: Activity?,
        id: String?,
        lastNonConfigurationInstance: Any?
    ): Activity {
        Log.i("FFFX","sffsd1")
        return super.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance)
    }
}