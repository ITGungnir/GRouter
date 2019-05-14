package test.itgungnir.grouter.common.router.interceptor

import my.itgungnir.grouter.api.dto.RouterResponse
import my.itgungnir.grouter.api.interceptor.BaseInterceptor
import my.itgungnir.grouter.api.interceptor.Interceptor
import test.itgungnir.grouter.common.IS_USER_CERT

class CertInterceptor(errorCallback: () -> Unit) : BaseInterceptor(errorCallback) {

    override fun intercept(chain: Interceptor.Chain): RouterResponse {

        return if (IS_USER_CERT) {
            chain.proceed(chain.request())
        } else {
            RouterResponse.failed(errorCallback)
        }
    }
}