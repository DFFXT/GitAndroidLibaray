package com.andoid.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.ImageReader
import android.media.projection.MediaProjection

/**
 * @author feiqin
 * @date 2021/11/3-18:48
 * @description 屏幕录制
 */
class ScreenRecorder(private val mediaProjection: MediaProjection) {
    var horizontalOffset = -1
    var width: Int = 0
    var height: Int = 0
    private lateinit var imageReader: ImageReader

    /**
     * 开始录制
     */
    fun startRecord(ctx: Context, callback: (Bitmap) -> Unit) {
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1)
        mediaProjection.createVirtualDisplay(
            "capture",
            width,
            height,
            ctx.resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION,
            // 输出surface
            imageReader.surface,
            null,
            null
        )
        imageReader.setOnImageAvailableListener({
            val image = it.acquireNextImage()
            // image.cropRect = Rect(100, 0, width -100, 200)
            val width = image.width
            val height = image.height
            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride // 每个像素占的宽度
            val rowStride = planes[0].rowStride // image 数据一行像素的宽度
            val rowPadding = rowStride - pixelStride * width // 一行像素的总间隔
            // bitmap 的像素需要算上间隔的像素：真实的像素+间隔占据的像素
            /*val newBuffer = ByteArray(width * height *4)
            val oldBuffer = ByteArray(rowStride * height)
            buffer.get(oldBuffer)
            for (i in 0 until height) {
                System.arraycopy(oldBuffer, rowPadding/2+i * rowStride, newBuffer, width * 4 * i, width * 4)
            }*/
            val bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            val b = Bitmap.createBitmap(bitmap, 0, 0, width, height)
            if (horizontalOffset < 0) {
                for (i in 0 until width) {
                    val color = b.getPixel(i, 0)
                    if (color != 0) {
                        horizontalOffset = i
                        break
                    }
                }
            }
            val realBitmap = Bitmap.createBitmap(b, horizontalOffset, 0, width - horizontalOffset * 2, height)
            image.close()
            callback.invoke(realBitmap)
            mediaProjection.stop()
        }, null)
    }
}