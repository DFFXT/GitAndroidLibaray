package com.fxffxt.media

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.andoid.media.ScreenRecorder
import com.andoid.media.SurfaceToMp4
import com.fxffxt.gitandroidlibaray.R
import com.fxffxt.gitandroidlibaray.TopWindow
import kotlin.random.Random

class ScreenRecordActivity : AppCompatActivity() {
    private lateinit var surfaceView: SurfaceView
    private var surfaceToMp4: SurfaceToMp4? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_record)
        surfaceView = findViewById(R.id.surfaceView)
        findViewById<View>(R.id.view).setOnClickListener {
            it.setBackgroundColor(Random(System.currentTimeMillis()).nextInt())
        }
        findViewById<View>(R.id.stop).setOnClickListener {
            surfaceToMp4?.stop()
        }
        //TopWindow.edit(this)
        /*surfaceView.post {
            TopWindow.v.findViewById<SurfaceView>(R.id.surfaceView).apply {
                surfaceView = this
            }
        }*/
        surfaceView.holder.setFormat(PixelFormat.TRANSLUCENT)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val service: MediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                startActivityForResult(service.createScreenCaptureIntent(), 120)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 120 && data != null) {
            val service: MediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            ScreenRecorder(service.getMediaProjection(resultCode, data)).apply {
                width = surfaceView.width
                height = this@ScreenRecordActivity.resources.displayMetrics.heightPixels
                surfaceToMp4 = SurfaceToMp4()
                val surface = surfaceToMp4!!.initEncoder(width, height, this@ScreenRecordActivity.externalCacheDir?.absolutePath + "/1.mp4")
                this.startRecord(this@ScreenRecordActivity) {
                    findViewById<ImageView>(R.id.bg).setImageBitmap(it)
                }
                surfaceToMp4?.start()

            }
        }
    }
}