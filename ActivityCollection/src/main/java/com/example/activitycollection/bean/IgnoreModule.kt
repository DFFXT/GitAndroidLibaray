package com.example.activitycollection.bean

/**
 * @author feiqin
 * @date 2021/11/8-15:08
 * @description 忽略类型的Module，如果模块入口返回的是这个对象，该模块的所有页面将不触发上报
 * @see DefaultActivityStackCollection.onPauseWithInfo
 */
class IgnoreModule(moduleId: String, childModuleId: String) : Module(moduleId, childModuleId)