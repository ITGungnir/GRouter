package my.itgungnir.grouter.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import my.itgungnir.grouter.api.dto.RouterRequest
import my.itgungnir.grouter.api.dto.RouterResponse
import my.itgungnir.grouter.api.interceptor.BaseInterceptor
import my.itgungnir.grouter.api.interceptor.Interceptor
import my.itgungnir.grouter.api.result.ProxyManager

/**
 * RouterManager
 */
class RouterManager(var request: RouterRequest) {

    /**
     * 携带参数，可调用多次
     */
    fun addParam(key: String, value: Any) = apply { request = request.addParam(key, value) }

    /**
     * 添加Flag，可调用多次
     */
    fun addFlag(flag: Int) = apply { request = request.addFlag(flag) }

    /**
     * 添加拦截器，可调用多次
     */
    fun addInterceptor(interceptor: BaseInterceptor) = apply {
        request = request.addInterceptor(interceptor)
    }

    /**
     * 通过请求获取RouterResponse对象
     */
    private fun getRouterResponse(): RouterResponse {
        // 整合系统和自定义的拦截器
        val chain = Interceptor.Chain(
            interceptors = request.interceptors + Router.instance.globalInterceptors(),
            index = 0, request = request
        )
        // 通过拦截器链生成RouterResponse
        return chain.proceed(request)
    }

    /**
     * 获取生成的Intent，仅能调用一次
     * 获取到的Intent可能为空
     */
    fun getIntent(): Intent? {
        val response = getRouterResponse()
        when (response.code) {
            RouterResponse.Code.ROUTE_SUCCEED -> Unit
            // 如果返回码为ROUTE_FAILED，调用拦截器中的Lambda表达式
            RouterResponse.Code.ROUTE_FAILED -> response.errorCallback.invoke()
        }
        return when (request.target) {
            is Uri -> response.intent
            else -> throw IllegalArgumentException("Unsupported target type.")
        }
    }

    /**
     * 跳转，可调用一次
     */
    fun go() {
        val context = request.safeContext()
        getIntent()?.let {
            when (context) {
                is Context -> context.startActivity(it)
                is Fragment -> context.startActivity(it)
            }
        }
    }

    /**
     * 跳转到指定页面
     * 相当于为目标页面配置了SingleTask启动模式
     * 如果目标Activity不存在，则在当前任务栈顶压入一个新的目标Activity
     */
    fun clearGo() {
        if (!Router.instance.routeMap().contains(request.target.toString())) {
            return
        }
        RouteTracker.instance.clearGo(Router.instance.routeMap()[request.target.toString()]!!) {
            go()
        }
    }

    /**
     * 相当于startActivityForResult()方法
     * 返回的是一个(RxJava)Observable对象
     */
    fun goForResult(requestCode: Int) = request.safeContext()?.let {
        getIntent()?.let { intent ->
            ProxyManager(it, intent).getResult(requestCode)
        }
    }
}
