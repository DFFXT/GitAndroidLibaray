package com.fxffxt.recyclerviewtoptixed

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.content.res.use
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * 顶部item常驻的RecyclerView容器
 * 只需adapter实现IAdapter接口即可
 * adapter的数据源一般要去掉第一个fixedItem数据
 */
open class TopFixedRecyclerViewWrapper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var recyclerView: RecyclerView
        private set
    lateinit var topSwitcher: ViewGroup
        private set
    protected lateinit var current: View
    protected lateinit var next: View

    init {
        orientation = VERTICAL
        context.obtainStyledAttributes(attrs, R.styleable.TopFixedRecyclerViewWrapper).use {
            val layout = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_layout, R.layout.layout_top_fixed_reyclerview)
            val rvId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_recyclerviewId, R.id.recyclerview)
            val topSwitcherId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_topSwitcherId, R.id.top_switcher)
            inflate(context, layout, this)
            findViewById<RecyclerView>(rvId).let { rv ->
                if (rv == null) {
                    throw Exception("没有根据id找到RecyclerView")
                }
                recyclerView = rv
            }
            findViewById<ViewGroup>(topSwitcherId).let { switcher ->
                if (switcher == null) {
                    throw Exception("没有根据id找到Switcher")
                }
                topSwitcher = switcher
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                refresh()
            }
        })
    }

    fun refresh(){
        val child = recyclerView.getChildAt(0)
        topSwitcher.children.forEach {
            it.visibility = if (child == null) View.INVISIBLE else View.VISIBLE
        }
        val position = recyclerView.getChildAdapterPosition(child)
        fixTopPeriodPhase(position, child)
    }


    protected open fun fixTopPeriodPhase(first: Int, firstView: View) {
        val adapter = (recyclerView.adapter as? IAdapter) ?: return
        if (adapter.isFixedItem(first)) {
            next.translationY = (topSwitcher.height + firstView.top).toFloat()
            adapter.bindFixedView(next, first)
            if (next.translationY > 0) {
                current.translationY = next.translationY - topSwitcher.height
            } else {
                current.translationY = topSwitcher.height + next.translationY
            }
            adapter.bindFixedView(current, first - 1)
        } else {
            adapter.bindFixedView(current, first)
            current.translationY = 0f
            next.translationY = topSwitcher.height.toFloat()
        }
    }

    /**
     * 在Adapter有数据时调用
     * @param viewType fixedItem所对应的viewType
     * @param forceRefresh 强制重新生成fixedItem
     * 给adapter设置数据时调用
     */
    fun buildTop(viewType: Int, forceRefresh: Boolean) {
        if (!forceRefresh && topSwitcher.childCount == 2) return
        val adapter = recyclerView.adapter ?: return
        val viewHolder1 = adapter.createViewHolder(recyclerView, viewType)
        val viewHolder2 = adapter.createViewHolder(recyclerView, viewType)
        (adapter as IAdapter).bindFixedView(viewHolder1.itemView, 0)
        topSwitcher.removeAllViews()
        current = viewHolder1.itemView
        next = viewHolder2.itemView
        topSwitcher.addView(viewHolder1.itemView)
        topSwitcher.addView(viewHolder2.itemView)
        viewHolder2.itemView.translationY = Float.MAX_VALUE
    }

}