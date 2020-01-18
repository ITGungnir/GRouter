package my.itgungnir.grouter.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import my.itgungnir.grouter.api.dto.RouterRequest
import my.itgungnir.grouter.api.dto.RouterResponse
import my.itgungnir.grouter.api.interceptor.BaseInterceptor
import my.itgungnir.grouter.api.interceptor.IntentGlobalInterceptor
import my.itgungnir.grouter.api.interceptor.Interceptor
import my.itgungnir.grouter.api.interceptor.VerifyGlobalInterceptor
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
    private fun getRouterResponse(): RouterResponse? {
        // 整合系统和自定义的拦截器
        val chain = Interceptor.Chain(
            interceptors = request.interceptors + VerifyGlobalInterceptor() +
                    Router.instance.globalInterceptors + IntentGlobalInterceptor(Router.instance.matchers),
            index = 0, request = request
        )
        // 通过拦截器链生成RouterResponse
        return chain.proceed(request)
    }

    /**
     * 获取生成的Intent，仅能调用一次
     * 获取到的Intent可能为空
     */
    fun getIntent(notMatchedCallback: (() -> Unit)? = null): Intent? {
        val response = getRouterResponse() ?: if (null == notMatchedCallback) {
            throw IllegalAccessException("No matcher found for this navigation request.")
        } else {
            notMatchedCallback.invoke()
            return null
        }
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
    fun go(notMatchedCallback: (() -> Unit)? = null) {
        val context = request.safeContext()
        getIntent(notMatchedCallback)?.let {
            when (context) {
                is Context -> context.startActivity(it)
                is Fragment -> context.startActivity(it)
            }
        }
    }

    /**
     * 相当于startActivityForResult()方法
     * 返回的是一个(RxJava)Observable对象
     */
    fun goForResult(requestCode: Int, notMatchedCallback: (() -> Unit)? = null) = request.safeContext()?.let {
        getIntent(notMatchedCallback)?.let { intent ->
            ProxyManager(it, intent).getResult(requestCode)
        }
    }
}
