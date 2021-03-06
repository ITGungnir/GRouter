package my.itgungnir.grouter.api.interceptor

import my.itgungnir.grouter.api.dto.RouterResponse
import my.itgungnir.grouter.api.matcher.BaseMatcher

/**
 * IntentInterceptor
 *
 * 最后一个拦截器，将请求处理成Intent对象
 * 通过Matcher列表匹配请求，生成不同类型的Intent
 */
class IntentGlobalInterceptor(private val matchers: Set<BaseMatcher>) : BaseGlobalInterceptor(priority = 1) {

    init {
        // 根据Matcher的优先级对Matcher列表进行排序
        matchers.sorted()
    }

    override fun intercept(chain: Interceptor.Chain): RouterResponse? {

        val request = chain.request()

        val matcher = matchers.find { it.matched(request) } ?: return null

        return RouterResponse.succeed(matcher.proceed(request))
    }
}
