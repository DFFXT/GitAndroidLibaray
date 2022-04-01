package com.fxffxt.stat

import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * @author feiqin
 * @date 2021/10/18-16:09
 * @description
 */
class DispatcherActivity : BaseStatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("page") == "1") {
            CommonActivity.actionStart(this)
            SingleTaskActivity.actionStart(this)
        } else {
            CommonActivity.actionStart(this)
            SingleTaskActivity1.actionStart(this)
        }
        finish()

    }

    companion object {
        @JvmStatic
        fun actionStart(ctx: Context) {
            val intent = Intent(ctx, DispatcherActivity::class.java)
            ctx.startActivity(intent)
        }
    }
}