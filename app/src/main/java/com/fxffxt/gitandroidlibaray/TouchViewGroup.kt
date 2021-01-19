package com.fxffxt.gitandroidlibaray

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.fxffxt.gitandroidlibaray.draw.DrawRect
import com.fxffxt.gitandroidlibaray.draw.ITouchDraw

class TouchViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var onConfigurationChange:((Configuration) -> Unit)?=null
    override fun onConfigurationChanged(newConfig: Configuration) {
        onConfigurationChange?.invoke(newConfig)
    }

    private val obj = ArrayList<ITouchDraw>()
    private val rect = DrawRect(Paint().apply {
        color = Color.RED
        strokeWidth = 3f
        style = Paint.Style.STROKE
    })
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        rect.onTouch(event)
        postInvalidate()
        return true
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        rect.draw(canvas)
    }
}