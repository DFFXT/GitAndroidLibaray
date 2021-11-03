package com.example.activitycollection

import com.example.activitycollection.base.IReport
import com.example.activitycollection.bean.Module

/**
 * @author yhlei@iflytek.com  create at 2021/7/16
 * @version 1.0
 * @date 2021/10/26
 * @description 多模块app需要在模块入口继承使用，单模块可以不继承，上报信息可以调用LearnReportLogsUploadManager.setDefaultParams来解决
 */
interface IDefaultAppLog : IReport<Module>