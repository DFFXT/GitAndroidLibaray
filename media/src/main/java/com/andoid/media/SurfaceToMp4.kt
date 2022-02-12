package com.andoid.media

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.view.Surface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author feiqin
 * @date 2021/11/4-13:42
 * @description 将surface上的数据转换成mp4
 */
class SurfaceToMp4 {
    private var trackIndex = 0
    private lateinit var encoder: MediaCodec
    private lateinit var muxer: MediaMuxer
    @Volatile
    private var isCancel = false

    private val bufferInfo = MediaCodec.BufferInfo()

    /**
     * 初始化
     */
    fun initEncoder(width: Int, height: Int, path: String): Surface {
        encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        val videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height)
        // COLOR_FormatSurface这里表明数据将是一个graphic buffer元数据
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        // 码率
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, 6000000)
        // 帧数
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
        // 关键帧间隔，预览时1s内都是同一张图
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        encoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        val surface = encoder.createInputSurface()

        encoder.start()
        muxer = MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        return surface
    }

    /**
     * 开始
     */
    fun start() {
        if (!::encoder.isInitialized) {
            throw Exception("not init")
        }
        GlobalScope.launch(Dispatchers.Default) {
            while (!isCancel) {
                loop(encoder, muxer)
            }
        }
    }

    /**
     * 停止
     */
    fun stop() {
        isCancel = true
    }

    private fun loop(encoder: MediaCodec, muxer: MediaMuxer) {
        val index = encoder.dequeueOutputBuffer(bufferInfo, 1000)
        if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            // setOutput format
            trackIndex = muxer.addTrack(encoder.outputFormat)
            muxer.start()
        } else if (index >= 0) {
            // 开始输出
            val buffer = encoder.getOutputBuffer(index)
            if (buffer != null) {
                buffer.position(bufferInfo.offset)
                buffer.limit(bufferInfo.offset + bufferInfo.size)
                muxer.writeSampleData(trackIndex, buffer, bufferInfo)
                // 释放buffer
                encoder.releaseOutputBuffer(index, false)
            }
        }
        if (isCancel) {
            encoder.release()
            muxer.release()
        }
    }
}