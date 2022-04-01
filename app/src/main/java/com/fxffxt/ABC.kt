package com.fxffxt

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.use
import com.fxffxt.gitandroidlibaray.R

/**
 * @author feiqin
 * @date 2022/2/25-13:42
 * @description
 */
class ABC @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ABC).use {
                val color = it.getColor(R.styleable.ABC_bg, Color.RED)
                setBackgroundColor(color)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}