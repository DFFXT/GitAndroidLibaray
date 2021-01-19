package com.fxffxt.recyclerviewtoptixed.shapeimageview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import com.fxffxt.recyclerviewtoptixed.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.*

/**
 * 将需要在style中创建的ShapeAppearanceModel移到View的属性里面来
 * 方便在布局xml里面修改
 */
class CustomShapeableImageView  : ShapeableImageView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle){

        builder(attrs, AbsoluteCornerSize(0f))?.let {
            shapeAppearanceModel = it.build()
        }
    }

    /**
     * 以下代码来自 ShapeAppearanceModel
     * @see ShapeAppearanceModel.Builder
     */
    private fun builder(attrs: AttributeSet?, defaultCornerSize: CornerSize): ShapeAppearanceModel.Builder? {
        attrs ?: return null
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomShapeableImageView)
        return try {
            val cornerFamily =
                    a.getInt(R.styleable.CustomShapeableImageView_civ_cornerFamily, CornerFamily.ROUNDED)
            val cornerFamilyTopLeft =
                    a.getInt(R.styleable.CustomShapeableImageView_civ_cornerFamilyTopLeft, cornerFamily)
            val cornerFamilyTopRight =
                    a.getInt(R.styleable.CustomShapeableImageView_civ_cornerFamilyTopRight, cornerFamily)
            val cornerFamilyBottomRight =
                    a.getInt(R.styleable.CustomShapeableImageView_civ_cornerFamilyBottomRight, cornerFamily)
            val cornerFamilyBottomLeft =
                    a.getInt(R.styleable.CustomShapeableImageView_civ_cornerFamilyBottomLeft, cornerFamily)
            val cornerSize =
                    getCornerSize(a, R.styleable.CustomShapeableImageView_civ_cornerSize, defaultCornerSize)
            val cornerSizeTopLeft =
                    getCornerSize(a, R.styleable.CustomShapeableImageView_civ_cornerSizeTopLeft, cornerSize)
            val cornerSizeTopRight =
                    getCornerSize(a, R.styleable.CustomShapeableImageView_civ_cornerSizeTopRight, cornerSize)
            val cornerSizeBottomRight =
                    getCornerSize(a, R.styleable.CustomShapeableImageView_civ_cornerSizeBottomRight, cornerSize)
            val cornerSizeBottomLeft =
                    getCornerSize(a, R.styleable.CustomShapeableImageView_civ_cornerSizeBottomLeft, cornerSize)
            ShapeAppearanceModel.Builder()
                    .setTopLeftCorner(cornerFamilyTopLeft, cornerSizeTopLeft)
                    .setTopRightCorner(cornerFamilyTopRight, cornerSizeTopRight)
                    .setBottomRightCorner(cornerFamilyBottomRight, cornerSizeBottomRight)
                    .setBottomLeftCorner(cornerFamilyBottomLeft, cornerSizeBottomLeft)
        } finally {
            a.recycle()
        }
    }

    private fun getCornerSize(a: TypedArray, index: Int, defaultValue: CornerSize): CornerSize {
        val value = a.peekValue(index) ?: return defaultValue
        return if (value.type == TypedValue.TYPE_DIMENSION) {
            // Eventually we might want to change this to call getDimension() since corner sizes support
            // floats.
            AbsoluteCornerSize(
                    TypedValue.complexToDimensionPixelSize(value.data, a.resources.displayMetrics)
                            .toFloat()
            )
        } else if (value.type == TypedValue.TYPE_FRACTION) {
            RelativeCornerSize(value.getFraction(1.0f, 1.0f))
        } else {
            defaultValue
        }
    }

}