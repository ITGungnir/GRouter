package my.itgungnir.grouter.api

import my.itgungnir.grouter.api.interceptor.IntentInterceptor
import my.itgungnir.grouter.api.interceptor.Interceptor
import my.itgungnir.grouter.api.interceptor.VerifyInterceptor
import my.itgungnir.grouter.api.matcher.BaseMatcher
import my.itgungnir.grouter.api.matcher.PathMatcher
import my.itgungnir.grouter.api.matcher.WebMatcher
import java.lang.ref.WeakReference

/**
 * 路由的入口类
 */
class Router private constructor() {

    // 项目的总路由表
    private val routeMap = hashMapOf<String, Class<*>>()

    // 非默认加载的路由表类名
    private val additionalRouteNames = mutableSetOf<String>()

    // 路由匹配器集合
    private val matchers = hashSetOf(
        PathMatcher(),
        WebMatcher()
    )

    // 全局路由拦截器集合
    private val globalInterceptors = mutableListOf(
        VerifyInterceptor(),
        IntentInterceptor(matchers)
    )

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
    fun registerGlobalInterceptor(globalInterceptor: Interceptor) {
        this.globalInterceptors.add(1, globalInterceptor)
    }

    /**
     * 注册非默认加载的路由表类名
     */
    fun registerAdditionalRouteMap(fileName: String) {
        additionalRouteNames.add(fileName)
    }

    fun routeMap() = routeMap

    fun additionalRouteMaps() = additionalRouteNames

    fun globalInterceptors() = globalInterceptors

    /**
     * 绑定生命周期
     */
    fun with(context: Any) = RouterBuilder(WeakReference(context))
}
