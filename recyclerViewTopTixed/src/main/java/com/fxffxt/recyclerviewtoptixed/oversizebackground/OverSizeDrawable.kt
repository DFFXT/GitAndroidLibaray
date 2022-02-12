package com.fxffxt.recyclerviewtoptixed.oversizebackground

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable

/**
 * @author feiqin
 * @date 2022/1/19-18:43
 * @description rippleDrawable
 */
class OverSizeDrawable(var start: Int, var top: Int, var end: Int, var bottom: Int, var drawable: Drawable?) : RippleDrawable(ColorStateList.valueOf(Color.TRANSPARENT), null, null) {

    override fun draw(canvas: Canvas) {
        drawable?.let {
            it.setBounds(canvas.clipBounds.left - start, canvas.clipBounds.top - top, canvas.clipBounds.right + end, canvas.clipBounds.bottom + bottom)
            it.draw(canvas)
        }
        super.draw(canvas)
    }
}