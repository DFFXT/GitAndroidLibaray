package com.fxffxt.gitandroidlibaray

import android.graphics.Color
import kotlin.random.Random

object TestData {
    fun getData():ArrayList<BaseItem>{
        val res = ArrayList<BaseItem>()
        val random = Random(System.currentTimeMillis())
        repeat(100){
            if (it % 4 == 0){
                res.add(FixedItem("index = $it", Color.RED))
            }else{
                res.add(CommonItem("index = $it",random.nextInt()))
            }
        }
        return res
    }
}

open class BaseItem(val text:String,val background:Int)
class CommonItem(text:String,background: Int):BaseItem(text,background)
class FixedItem(text:String, background: Int):BaseItem(text,background)
//找到[0,position]位置的最后一个FixedItem
fun List<BaseItem>.findFixedItem(position:Int):FixedItem?{
    if (position !in indices) return null
   for (i in position downTo 0){
       if (this[i] is FixedItem){
           return this[i] as FixedItem
       }
   }
    return null
}