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

class FixedInnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fixed_inner)
        val layout = findViewById<TopFixedRecyclerViewWrapper>(R.id.topFixedRecyclerViewWrapper)
        val data = TestData.getData()
        val adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(),IAdapter{
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val v = TextView(this@FixedInnerActivity)
                v.layoutParams = ViewGroup.LayoutParams(-1,100)
                return object :RecyclerView.ViewHolder(v){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val tv = holder.itemView as TextView
                tv.text = data[position].text
                tv.setBackgroundColor(data[position].background)
            }

            override fun getItemCount(): Int = data.size
            override fun isFixedItem(position: Int): Boolean {
                val position = position +1
                return data[position] is FixedItem
            }

            override fun bindFixedView(itemView: View, position: Int) {
                val position = position +1
                val item = data.findFixedItem(position)?:return
                (itemView as TextView).text = item.text
                itemView.setBackgroundColor(item.background)
            }
        }
        layout.recyclerView.layoutManager = LinearLayoutManager(this)
        layout.recyclerView.adapter = adapter
        layout.buildTop(-1,false)
    }
}
