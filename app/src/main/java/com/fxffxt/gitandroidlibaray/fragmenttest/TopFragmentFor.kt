package com.fxffxt.gitandroidlibaray.fragmenttest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.StartForResultFragment
import com.fxffxt.gitandroidlibaray.R

class TopFragmentFor:StartForResultFragment() {
    private var send = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top,container,false)
    }
    lateinit var text: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tv = view.findViewById<TextView>(R.id.button)
        text = System.currentTimeMillis().toString()
        tv.text = text
        tv.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment,TopFragmentFor())
                .commitNow()
            send = true
        }
        view.findViewById<View>(R.id.jumpActivity).setOnClickListener {
            startActivityForResult(Intent(requireContext(),DestActivity::class.java),123)
        }
    }

    override fun onSupportActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onSupportActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            Toast.makeText(requireContext(),text,Toast.LENGTH_LONG).show()
        }
    }
}