package my.itgungnir.grouter.api.dto

import android.net.Uri
import android.os.Bundle
import my.itgungnir.grouter.api.interceptor.BaseInterceptor
import java.lang.ref.WeakReference

/**
 * 路由信息的封装类
 */
data class RouterRequest(
    // 路由的发出方，可能是Context或Fragment，使用WeakReference包裹
    val context: WeakReference<Any>? = null,
    // 路由的到达方，Uri类型
    val target: Uri? = null,
    // 路由携带的参数
    val extras: Bundle = Bundle(),
    // 路由的Flags
    val flags: Int = 0,
    // 路由添加的自定义拦截器
    val interceptors: List<BaseInterceptor> = listOf()
) {

    fun target(target: Uri) = this.copy(target = target)

    fun addParam(key: String, value: Any) = this.copy(extras = extras.apply {
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
        }
    })

    fun addFlag(flag: Int) = this.copy(flags = flags or flag)

    fun addInterceptor(interceptor: BaseInterceptor) =
        this.copy(interceptors = interceptors + interceptor)

    fun safeContext() = context?.get()
}
