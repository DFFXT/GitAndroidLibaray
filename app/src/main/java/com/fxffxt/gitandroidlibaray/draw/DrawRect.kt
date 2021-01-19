package com.fxffxt.gitandroidlibaray.draw

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

class DrawRect (val paint: Paint = Paint()):ITouchDraw (){
    private var downX:Float = 0f
    private var downY:Float = 0f
    private var endX:Float = 0f
    private var endY:Float = 0f
    private var init = false
    override fun onTouch(e: MotionEvent) {
        /*if (!init){
            initRect(e)
        }
        if (mutable){
            move(e)
        }*/
        initRect(e)

    }

    override fun pointIn(x: Float, y: Float):Boolean {
        return x > downX && x < endY && y > downY && y < endY
    }
    private fun initRect(e:MotionEvent){
        val x = e.x
        val y = e.y
        when(e.action){
            MotionEvent.ACTION_DOWN ->{
                downX = x
                downY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL->{
                mutable = false
                init = true
            }
        }
        endX = x
        endY = y
    }
    private fun move(e:MotionEvent){
        /*when(e.action){
            MotionEvent.ACTION_DOWN ->{
                downX = x
                downY = y
            }
        }*/
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(downX,downY,endX,endY,paint)
    }

}