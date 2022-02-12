package com.fxffxt.recyclerviewtoptixed.oversizebackground

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.fxffxt.recyclerviewtoptixed.R
import kotlin.math.roundToInt

/**
 * @author feiqin
 * @date 2022/1/19-17:14
 * @description 让背景图可以超出View显示，采用rippleDrawable来实现，一般用于显示阴影切图
 */
class OverSizeView : ConstraintLayout {
    private var offsetStart = TypedValue()
    private var offsetTop = TypedValue()
    private var offsetEnd = TypedValue()
    private var offsetBottom = TypedValue()
    private val oldSize = intArrayOf(-1, -1)
    private lateinit var rippleDrawable: OverSizeDrawable

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let {
            rippleDrawable = OverSizeDrawable(0, 0, 0, 0, null)
            context.obtainStyledAttributes(it, R.styleable.OverSizeView).use { typedArray ->
                typedArray.getValue(R.styleable.OverSizeView_osv_offsetStart, offsetStart)
                typedArray.getValue(R.styleable.OverSizeView_osv_offsetTop, offsetTop)
                typedArray.getValue(R.styleable.OverSizeView_osv_offsetEnd, offsetEnd)
                typedArray.getValue(R.styleable.OverSizeView_osv_offsetBottom, offsetBottom)
            }
        }
        background = background
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // 宽高变化触发offset变化
        if (right - left != oldSize[0] || bottom - top != oldSize[1]) {
            oldSize[0] = right - left
            oldSize[1] = bottom - top
            rippleDrawable.start = getOffset(offsetStart)
            rippleDrawable.top = getOffset(offsetTop)
            rippleDrawable.end = getOffset(offsetEnd)
            rippleDrawable.bottom = getOffset(offsetBottom)
        }
    }

    private fun getOffset(typedValue: TypedValue): Int {
        if (typedValue.type == TypedValue.TYPE_DIMENSION) {
            return TypedValue.complexToDimensionPixelOffset(typedValue.data, resources.displayMetrics)
        } else if (typedValue.type == TypedValue.TYPE_FRACTION) {
            // 获取父布局的width，没有parent则使用自己的width
            val pWidth = (parent as? ViewGroup)?.width?.toFloat() ?: width.toFloat()
            return TypedValue.complexToFraction(typedValue.data, width.toFloat(), pWidth).roundToInt()
        }
        return 0
    }

    override fun setBackground(background: Drawable?) {
        // 父类构造方法中会调用setBackground，此时rippleDrawable还未初始化
        if (background != null && background !is RippleDrawable && this::rippleDrawable.isInitialized) {
            rippleDrawable.drawable = background
            super.setBackground(rippleDrawable)
        } else {
            super.setBackground(background)
        }
    }
}