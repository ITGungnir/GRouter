package my.itgungnir.grouter.api.dto

import android.content.Intent

/**
 * RouterRequest对象经过所有拦截器之后得到的结果
 */
data class RouterResponse(
    // 路由结果：SUCCEED成功、FAILED失败
    val code: Code,
    // 如果成功，则intent不为空
    val intent: Intent? = null,
    // 如果失败，则intent为空，errorCallback不为空
    val errorCallback: () -> Unit = {}
) {

    companion object {
        /**
         * 成功
         */
        fun succeed(intent: Intent) =
            RouterResponse(
                Code.ROUTE_SUCCEED,
                intent
            )

        /**
         * 失败
         */
        fun failed(errorCallback: () -> Unit) =
            RouterResponse(
                Code.ROUTE_FAILED,
                null,
                errorCallback
            )
    }

    enum class Code {
        ROUTE_SUCCEED,
        ROUTE_FAILED
    }
}
