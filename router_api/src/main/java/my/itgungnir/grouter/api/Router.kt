package my.itgungnir.grouter.api

import my.itgungnir.grouter.api.interceptor.BaseGlobalInterceptor
import my.itgungnir.grouter.api.matcher.BaseMatcher
import my.itgungnir.grouter.api.matcher.DeepLinkMatcher
import my.itgungnir.grouter.api.matcher.RouteMapMatcher
import java.lang.ref.WeakReference

/**
 * 路由的入口类
 */
class Router private constructor() {

    // 项目的总路由表
    val routeMap = hashMapOf<String, Class<*>>()

    // 非默认加载的路由表类名
    val additionalRouteNames = mutableSetOf<String>()

    // 路由匹配器集合
    val matchers = hashSetOf(
        RouteMapMatcher(),
        DeepLinkMatcher()
    )

    // 全局路由拦截器集合
    val globalInterceptors = sortedSetOf<BaseGlobalInterceptor>()

    companion object {
        val instance by lazy { Router() }
    }

    /**
     * 注册路由信息
     */
    fun registerRoute(path: String, clz: Class<*>) {
        routeMap[path] = clz
    }

    /**
     * 注册路由匹配器
     */
    fun registerMatcher(matcher: BaseMatcher) {
        this.matchers.add(matcher)
    }

    /**
     * 注册全局路由拦截器
     */
    fun registerGlobalInterceptor(globalInterceptor: BaseGlobalInterceptor) {
        this.globalInterceptors.add(globalInterceptor)
    }

    /**
     * 注册非默认加载的路由表类名
     */
    fun registerAdditionalRouteMap(fileName: String) {
        additionalRouteNames.add(fileName)
    }

    /**
     * 绑定生命周期
     */
    fun with(context: Any) = RouterBuilder(WeakReference(context))
}
