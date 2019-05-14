package my.itgungnir.grouter.api.interceptor

import my.itgungnir.grouter.api.dto.RouterRequest
import my.itgungnir.grouter.api.dto.RouterResponse

/**
 * 拦截器接口
 *
 * 所有系统拦截器和自定义拦截器都直接或间接实现该接口
 */
interface Interceptor {

    fun intercept(chain: Chain): RouterResponse

    /**
     * 拦截器链
     */
    class Chain(
        // 所有拦截器的列表
        private val interceptors: List<Interceptor>,
        // 当前拦截器所在位置
        private val index: Int,
        // 要处理的路由请求对象
        private val request: RouterRequest
    ) {

        fun request(): RouterRequest = this.request

        fun proceed(request: RouterRequest): RouterResponse {

            if (this.index >= interceptors.size) {
                throw AssertionError()
            }

            val next = Chain(
                interceptors,
                index + 1,
                request
            )

            val interceptor = interceptors[index]

            return interceptor.intercept(next)
        }
    }
}