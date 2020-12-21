package com.fxffxt.gitandroidlibaray

import android.graphics.Color
import kotlin.random.Random

object TestData {
    fun getData(len:Int = 100):ArrayList<BaseItem>{
        val res = ArrayList<BaseItem>()
        val random = Random(System.currentTimeMillis())
        repeat(len){
            if (it % 4 == 0){
                res.add(FixedItem("index = $it", Color.WHITE,50))
            }else{
                res.add(CommonItem("index = $it",random.nextInt(),100))
            }
        }
        return res
    }
}

open class BaseItem(val text:String,val background:Int,val height: Int)
class CommonItem(text:String,background: Int,height:Int):BaseItem(text,background,height)
class FixedItem(text:String, background: Int,height:Int):BaseItem(text,background,height)
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