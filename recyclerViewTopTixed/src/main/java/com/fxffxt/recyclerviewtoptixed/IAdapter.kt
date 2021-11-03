package com.fxffxt.recyclerviewtoptixed

import android.view.View

/**
 * adapter需要实现的接口
 */
interface IAdapter {
    /**
     * 判断改item是否是需要显示的顶部item
     */
    fun isFixedItem(position: Int): Boolean

    /**
     * 给itemView绑定当前position所属的fixedItem的视图
     */
    fun bindFixedView(itemView: View, position: Int)
}