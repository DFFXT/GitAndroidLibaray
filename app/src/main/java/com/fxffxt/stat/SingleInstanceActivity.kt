package com.fxffxt.stat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fxffxt.gitandroidlibaray.R
import com.example.activitycollection.ext.removeActivityInfo

/**
 * @author feiqin
 * @date 2021/10/19-14:59
 * @description
 */
class SingleInstanceActivity : BaseStatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.btn_actSingle).setOnClickListener {
            SingleInstanceActivity.actionStart(this)
        }
        findViewById<View>(R.id.btn_actCommon).setOnClickListener {
            CommonActivity.actionStart(this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        removeActivityInfo()
    }

    companion object {
        @JvmStatic
        fun actionStart(ctx: Context) {
            val intent = Intent(ctx, SingleInstanceActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        }
    }
}