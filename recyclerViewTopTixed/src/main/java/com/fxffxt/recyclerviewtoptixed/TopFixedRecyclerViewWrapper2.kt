package com.fxffxt.recyclerviewtoptixed

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.use
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView顶部常驻item容器
 * 在常驻item有不透明背景时，TopFixedRecyclerViewWrapper2优于TopFixedRecyclerViewWrapper
 * 不需要像TopFixedRecyclerViewWrapper那样需要将数据的第一个fixedItem去掉
 * TopFixedRecyclerViewWrapper2的顶部是覆盖在RecyclerView上的
 * TopFixedRecyclerViewWrapper的顶部是在RecyclerView的外部，一般用于fixedItem的背景不透明
 */
open class TopFixedRecyclerViewWrapper2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var recyclerView: RecyclerView
        private set
    lateinit var topSwitcher: ViewGroup
        private set
    protected lateinit var current: View

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TopFixedRecyclerViewWrapper2).use {
            val layout = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper2_wrapper2_layout, R.layout.layout_top_fixed_reyclerview2)
            val rvId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper2_wrapper2_recyclerviewId, R.id.recyclerview)
            val topSwitcherId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper2_wrapper2_topSwitcherId, R.id.top_switcher)
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
        topSwitcher.visibility = if (child == null) View.INVISIBLE else View.VISIBLE
        val position = recyclerView.getChildAdapterPosition(child)
        fixTopPeriodPhase(position)
    }


    protected open fun fixTopPeriodPhase(first: Int) {
        val adapter = (recyclerView.adapter as? IAdapter) ?: return
        adapter.bindFixedView(current, first)
        var fixedItemView: View? = null
        for (childIndex in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(childIndex)
            val position = recyclerView.getChildAdapterPosition(child)
            if (position < 0) continue
            if (adapter.isFixedItem(position)) {
                fixedItemView = child
                break
            }
        }
        if (fixedItemView != null) {
            if (fixedItemView.top > topSwitcher.height || fixedItemView.top <= 0) {
                current.translationY = 0f
            } else {
                current.translationY = (fixedItemView.top - topSwitcher.height).toFloat()
            }
        } else {
            current.translationY = 0f
        }
    }

    /**
     * 在Adapter有数据时调用
     * @param viewType fixedItem所对应的viewType
     * @param forceRefresh 强制重新生成fixedItem，一般在FixedItem的布局类型发生变化时才为true
     * 给adapter设置数据时调用
     */
    fun buildTop(viewType: Int, forceRefresh: Boolean) {
        if (!forceRefresh && topSwitcher.childCount == 1) return
        val adapter = recyclerView.adapter ?: return
        val viewHolder1 = adapter.createViewHolder(recyclerView, viewType)
        (adapter as IAdapter).bindFixedView(viewHolder1.itemView, 0)
        topSwitcher.removeAllViews()
        current = viewHolder1.itemView
        topSwitcher.addView(viewHolder1.itemView)
    }
}