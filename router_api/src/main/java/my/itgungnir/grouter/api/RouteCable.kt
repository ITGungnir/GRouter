package my.itgungnir.grouter.api

import android.app.Application

/**
 * 路由总线
 *
 * 用于整合多个模块中的路由信息
 */
interface RouteCable {

    fun init(context: Application)
}