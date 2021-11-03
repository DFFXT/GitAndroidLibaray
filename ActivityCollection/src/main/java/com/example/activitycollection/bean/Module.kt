package com.example.activitycollection.bean

import com.example.activitycollection.base.Cloneable

/**
 * @author feiqin
 * @date 2021/10/26-14:39
 * @description 应用模块信息，扩展时一般都时继承该类来扩展，如果直接接触Cloneable，还需要实现ParamsBuilder
 */
open class Module(var moduleId: String, var childModuleId: String) : Cloneable {
    final override fun <T : Cloneable> clone(): T {
        return createNewObject() as T
    }
    open fun createNewObject(): Module {
        return Module(moduleId, childModuleId)
    }
}