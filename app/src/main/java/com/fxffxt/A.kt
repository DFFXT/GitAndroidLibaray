package com.fxffxt

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author feiqin
 * @date 2022/3/31-17:25
 * @description
 */
class A {
    init {
        GlobalScope.launch {
            //delay(12)
            go()
            delay(1)
            go1()
            delay(2)
            go2()
            val f1 = async {
                111
            }
            val f2 = async {
                1222
            }
            val t = f1.await() + f2.await()
        }
    }
    suspend fun go() {
        val f = 0
    }
    suspend fun go1() {
        go2()
    }
    suspend fun go2() {
        val f = 0
    }
}