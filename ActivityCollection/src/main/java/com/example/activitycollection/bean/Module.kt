package com.example.activitycollection.bean

import com.example.activitycollection.base.Cloneable

/**
 * @author feiqin
 * @date 2021/10/26-14:39
 * @description 应用模块信息，扩展时一般都时继承该类来扩展，如果直接继承Cloneable，还需要实现ParamsBuilder
 */
open class Module(
    var moduleId: String,
    var childModuleId: String
) : Cloneable