package com.fxffxt.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fxffxt.gitandroidlibaray.R

/**
 * @author feiqin
 * @date 2021/12/8-11:49
 * @description
 */
class TestFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}