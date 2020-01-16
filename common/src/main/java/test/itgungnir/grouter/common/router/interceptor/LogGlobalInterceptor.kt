package test.itgungnir.grouter.common.router.interceptor

import my.itgungnir.grouter.annotation.GlobalInterceptor
import my.itgungnir.grouter.api.dto.RouterResponse
import my.itgungnir.grouter.api.interceptor.BaseGlobalInterceptor
import my.itgungnir.grouter.api.interceptor.Interceptor

@GlobalInterceptor
class LogGlobalInterceptor : BaseGlobalInterceptor(priority = 1) {

    override fun intercept(chain: Interceptor.Chain): RouterResponse {

        val request = chain.request()

        println("------>>Router: ${request.safeContext()!!::class.java.name} -> ${request.target.toString()}")

        return chain.proceed(request)
    }
}
