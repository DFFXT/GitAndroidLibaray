package com.fxffxt.gitandroidlibaray

import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

object TopWindow {
    private lateinit var ctx:Application
    val v by lazy {  LayoutInflater.from(ctx).inflate(R.layout.wm, null, false)}
    private var lp = WindowManager.LayoutParams()
    private val wm by lazy {  ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager}
    private fun init (){

        //init {
            if (Build.VERSION.SDK_INT> Build.VERSION_CODES.O){

                lp.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }else{
                lp.type= WindowManager.LayoutParams.TYPE_PHONE
            }
            lp.format = PixelFormat.TRANSPARENT

            lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //or  WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            lp.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            val p = Point()
            wm.defaultDisplay?.getRealSize(p)
            lp.height = p.y
            lp.gravity = Gravity.TOP
            lp.width = p.x
            wm.addView(v, lp)
            (v as TouchViewGroup).onConfigurationChange = {
                lp = v.layoutParams as WindowManager.LayoutParams
                wm.defaultDisplay?.getRealSize(p)
                lp.height = p.y
                lp.width = p.x
                wm.updateViewLayout(v, lp)
            }
            DragHelper(v.findViewById(R.id.iv_icon)).bindDrag()
            DragHelper(v.findViewById(R.id.iv_icon1)).bindDrag()
            DragHelper(v.findViewById(R.id.iv_icon2)).bindDrag()
        //}
    }


    fun edit(lifecycleOwner: LifecycleOwner){
        ctx = (lifecycleOwner as Context).applicationContext as Application
        init()
        lifecycleOwner.lifecycle.addObserver(object :LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS //or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    wm.updateViewLayout(v, lp)
                }else if (event == Lifecycle.Event.ON_PAUSE){
                    lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    wm.updateViewLayout(v, lp)
                }
            }
        })
    }
}