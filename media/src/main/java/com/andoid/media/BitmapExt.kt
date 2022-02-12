package com.andoid.media

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

/**
 * @author feiqin
 * @date 2021/11/5-10:35
 * @description
 */

fun Bitmap.to565(): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val c = Canvas(b)
    val p = Paint()
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    p.colorFilter = ColorMatrixColorFilter(matrix)
    c.drawBitmap(this, 0f, 0f, p)
    return b
}