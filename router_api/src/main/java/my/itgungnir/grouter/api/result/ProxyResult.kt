package my.itgungnir.grouter.api.result

import android.os.Bundle

/**
 * startActivityForResult的返回结果
 */
data class ProxyResult(val code: ResultCode, val extras: Bundle = Bundle()) {

    enum class ResultCode {
        RESULT_OK,
        RESULT_CANCELED
    }
}