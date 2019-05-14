package my.itgungnir.grouter.api

import android.net.Uri
import my.itgungnir.grouter.api.dto.RouterRequest
import java.lang.ref.WeakReference

/**
 * RouterBuilder
 */
class RouterBuilder(val context: WeakReference<Any>) {

    // 初始化RouterRequest对象
    private var request = RouterRequest(context = context)

    /**
     * 绑定路由到达方
     * 传入一个String，方法内转换成Uri对象
     */
    fun target(target: String): RouterManager {

        request = request.target(Uri.parse(target))

        return RouterManager(request)
    }
}