package com.fxffxt.gitandroidlibaray

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_inner).setOnClickListener {
            startActivity(Intent(this, FixedInnerActivity::class.java))
        }
        findViewById<View>(R.id.tv_outer).setOnClickListener {
            startActivity(Intent(this, FixedOuterActivity::class.java))
        }

    }
}
