package com.fxffxt.gitandroidlibaray.fragmenttest

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DestActivity: AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "sssssssssss"
            setOnClickListener {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("data","dataValue")
                })
                finish()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}