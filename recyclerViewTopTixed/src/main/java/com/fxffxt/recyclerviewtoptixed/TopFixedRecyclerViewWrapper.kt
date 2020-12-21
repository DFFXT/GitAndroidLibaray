package com.fxffxt.recyclerviewtoptixed

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * 两种方式顶部常驻
 * 顶部item常驻的RecyclerView容器
 * 只需adapter实现IAdapter接口即可
 * 类型1：如果自定义layout 类似于  R.layout.layout_top_fixed_reyclerview_outer：
 *      adapter的数据源一般要去掉第一个fixedItem数据
 * 类型2（默认）：如果自定义layout 类似于 R.layout.layout_top_fixed_reyclerview_inner：
 *
 * 最终还是要视实现的效果而定
 */
open class TopFixedRecyclerViewWrapper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var recyclerView: RecyclerView
        private set
    lateinit var topSwitcher: ViewGroup
        private set
    protected lateinit var current: View
    protected lateinit var next: View

    /**
     * 当FixedItem的布局不一致时（不同FixedItem高度不一样），如果snap<1有可能导致闪烁
     */
    var snap = 1f
    private var fixedItemOver = true

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TopFixedRecyclerViewWrapper).use {
            val layout = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_layout, R.layout.layout_top_fixed_reyclerview_inner)
            val rvId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_recyclerviewId, R.id.recyclerview)
            val topSwitcherId = it.getResourceId(R.styleable.TopFixedRecyclerViewWrapper_wrapper_topSwitcherId, R.id.top_switcher)
            snap = it.getFloat(R.styleable.TopFixedRecyclerViewWrapper_wrapper_snap, snap)
            fixedItemOver = it.getBoolean(R.styleable.TopFixedRecyclerViewWrapper_wrapper_fixedItemOver, fixedItemOver)
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
        if (fixedItemOver){
            fixedItemOverRecyclerView()
        }else{
            fixedItemOutsideRecyclerView()
        }
    }


    protected open fun fixedItemOverRecyclerView() {
        val adapter = (recyclerView.adapter as? IAdapter) ?: return
        for (i in 0 until recyclerView.childCount){
            val child = recyclerView.getChildAt(i)
            val index = recyclerView.getChildAdapterPosition(child)
            if (index < 0 )continue
            if (i == 0){
                adapter.bindFixedView(current, index)
                current.translationY = 0f
                next.translationY = Float.MAX_VALUE
            }else if (adapter.isFixedItem(index)){
                if (child.top < current.height){
                    adapter.bindFixedView(next, index)
                    next.translationY = child.top.toFloat()
                    current.translationY = -(current.height - next.translationY)*snap
                }
                break
            }
        }
    }
    protected open fun fixedItemOutsideRecyclerView() {
        val adapter = (recyclerView.adapter as? IAdapter) ?: return
        val firstView = recyclerView.getChildAt(0)?:return
        val first = recyclerView.getChildAdapterPosition(firstView)
        if (adapter.isFixedItem(first)) {
            next.translationY = (topSwitcher.height + firstView.top).toFloat()
            adapter.bindFixedView(next, first)
            if (next.translationY > 0) {
                current.translationY = (next.translationY - topSwitcher.height) * snap
            } else {
                current.translationY = (topSwitcher.height + next.translationY) * snap
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
        if (!forceRefresh && (::current.isInitialized || ::next.isInitialized)) return
        val adapter = recyclerView.adapter ?: return
        val viewHolder1 = adapter.createViewHolder(recyclerView, viewType)
        val viewHolder2 = adapter.createViewHolder(recyclerView, viewType)
        (adapter as IAdapter).bindFixedView(viewHolder1.itemView, 0)
        if (::current.isInitialized){
            topSwitcher.removeView(current)
            topSwitcher.removeView(next)
        }
        current = viewHolder1.itemView
        next = viewHolder2.itemView
        topSwitcher.addView(viewHolder1.itemView)
        topSwitcher.addView(viewHolder2.itemView)
        viewHolder2.itemView.translationY = Float.MAX_VALUE
    }

}