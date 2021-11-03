package com.fxffxt.gitandroidlibaray

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * 使一个View能够在父布局中自由拖动
 */
class DragHelper(private val target: View, var dragLimit: DragLimit = DragLimit()) {
    private val parent: ViewGroup
        get() = target.parent as ViewGroup

    /**
     * 绑定drag
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindDrag(): DragHelper {
        val ctx = target.context

        // Drag
        var downX = 0f
        var downY = 0f
        var preX = 0f
        var preY = 0f
        var moved = false
        val touchSlop = ViewConfiguration.get(ctx).scaledTouchSlop
        target.setOnTouchListener { _, event ->
            val x = event.rawX
            val y = event.rawY
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = x
                    downY = y
                    preX = x
                    preY = y
                    moved = false
                }
                MotionEvent.ACTION_MOVE -> {
                    if (moved || abs(x - downX) > touchSlop || abs(y - downY) > touchSlop) {

                        // 限制超出屏幕部分 方法2也可行
                        val leftX = max(0 - (target.x - dragLimit.paddingStart + x - preX), 0f)
                        val rightX = min(
                            parent.width - (target.x + target.width + dragLimit.paddingEnd + x - preX),
                            0f
                        )
                        val moreX = leftX + rightX

                        val topY = max(0 - (target.y - dragLimit.paddingTop + y - preY), 0f)
                        val bottomY = min(
                            parent.height - (target.y + target.height + dragLimit.paddingBottom + y - preY),
                            0f
                        )
                        val moreY = topY + bottomY

                        target.translationX += x - preX + moreX
                        target.translationY += y - preY + moreY
                        moved = true
                        preX = x + moreX
                        preY = y + moreY
                    } else {
                        preX = x
                        preY = y
                    }
                }

                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    if (!moved) {
                        target.performClick()
                    }
                    moved = false
                }
                else -> return@setOnTouchListener false
            }
            return@setOnTouchListener true
        }
        return this
    }

    // 限制拖拽对象在parent中的活动空间，可为负
    class DragLimit(var paddingStart: Int = 0, var paddingTop: Int = 0, var paddingEnd: Int = 0, var paddingBottom: Int = 0) {
        /**
         * 重载+操作符
         */
        operator fun plus(dragLimit: DragLimit): DragLimit {
            return DragLimit(
                paddingStart + dragLimit.paddingStart,
                paddingTop + dragLimit.paddingTop,
                paddingEnd + dragLimit.paddingEnd,
                paddingBottom + dragLimit.paddingBottom
            )
        }
    }
}