package com.fxffxt.gitandroidlibaray

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fxffxt.recyclerviewtoptixed.IAdapter
import com.fxffxt.recyclerviewtoptixed.TopFixedRecyclerViewWrapper

class FixedOuterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fixed_outer)
        val layout = findViewById<TopFixedRecyclerViewWrapper>(R.id.topFixedRecyclerViewWrapper)
        val originData = TestData.getData()
        /**
         * 这里需要将第一个FixedItem移除
         */
        val data = ArrayList(originData)
        data.removeAt(0)
        val adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(),IAdapter{
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val v = TextView(this@FixedOuterActivity)
                v.layoutParams = ViewGroup.LayoutParams(-1,100)
                return object :RecyclerView.ViewHolder(v){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val tv = holder.itemView as TextView
                tv.text = data[position].text
                tv.setBackgroundColor(data[position].background)
            }

            override fun getItemCount(): Int =data.size
            override fun isFixedItem(position: Int): Boolean {
                return data[position] is FixedItem
            }

            override fun bindFixedView(itemView: View, position: Int) {
                //这里需要+1
                val item = originData.findFixedItem(position + 1)?:return
                (itemView as TextView).text = item.text
                itemView.setBackgroundColor(item.background)
            }
        }
        layout.recyclerView.layoutManager = LinearLayoutManager(this)
        layout.recyclerView.adapter = adapter
        layout.buildTop(-1,false)
    }
}
