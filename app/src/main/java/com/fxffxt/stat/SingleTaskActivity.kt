package com.fxffxt.stat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import com.fxffxt.gitandroidlibaray.R

/**
 * @author feiqin
 * @date 2021/10/18-16:09
 * @description
 */
class SingleTaskActivity : BaseStatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.btn_actSingle).setOnClickListener {
            SingleInstanceActivity.actionStart(this)
        }
        findViewById<View>(R.id.btn_actCommon).setOnClickListener {
            CommonActivity.actionStart(this)
        }
    }

    companion object {
        @JvmStatic
        fun actionStart(ctx: Context) {
            val intent = Intent(ctx, SingleTaskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        }
    }
}