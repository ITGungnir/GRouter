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

        lazyLoadRouteMapsIfNeeded(target)

        request = request.target(Uri.parse(target))

        return RouterManager(request)
    }

    private fun lazyLoadRouteMapsIfNeeded(target: String) {
        if (!target.startsWith("/") || isDefaultRoute(target)) {
            return
        }
        if (Router.instance.routeMap().containsKey(target)) {
            return
        }
        loadAdditionalRouteMaps(target)
    }

    private fun isDefaultRoute(target: String) = !target.substring(1).contains("/")

    private fun loadAdditionalRouteMaps(target: String) {
        val fileNames = Router.instance.additionalRouteMaps().filter { it.endsWith("Route4${getRouteGroup(target)}") }
        if (fileNames.isNullOrEmpty()) {
            return
        }
        fileNames.forEach { fileName -> registerAdditionalRoutes(fileName) }
    }

    private fun getRouteGroup(target: String) = target.substring(1).split("/")[0]

    private fun registerAdditionalRoutes(fileName: String) {
        Class.forName(fileName).apply { getDeclaredMethod("register").invoke(newInstance()) }
    }
}