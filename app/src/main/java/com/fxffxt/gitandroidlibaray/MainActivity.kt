package com.fxffxt.gitandroidlibaray

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fxffxt.recyclerviewtoptixed.IAdapter
import com.fxffxt.recyclerviewtoptixed.TopFixedRecyclerViewWrapper2
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layout = findViewById<TopFixedRecyclerViewWrapper2>(R.id.topFixedRecyclerViewWrapper2)
        val adapter = object :RecyclerView.Adapter<RecyclerView.ViewHolder>(),IAdapter{
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val v = TextView(this@MainActivity)
                v.layoutParams = ViewGroup.LayoutParams(-1,100)
                return object :RecyclerView.ViewHolder(v){}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val tv = holder.itemView as TextView
                tv.text = "$position"
                tv.setBackgroundColor(Random(System.currentTimeMillis()).nextInt())
                if (position% 4==0) tv.setBackgroundColor(Color.RED)
            }

            override fun getItemCount(): Int = 100
            override fun isFixedItem(position: Int): Boolean {
                return (position%4==0)
            }

            override fun bindFixedView(itemView: View, position: Int) {
                (itemView as TextView).text = (position- position%4).toString()
                itemView.setBackgroundColor(Color.RED)
            }
        }
        layout.recyclerView.layoutManager = LinearLayoutManager(this)
        layout.recyclerView.adapter = adapter
        layout.refreshTop(-1,false)
    }
}
