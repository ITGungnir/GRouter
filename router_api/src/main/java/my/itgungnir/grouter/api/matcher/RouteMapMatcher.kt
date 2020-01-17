package my.itgungnir.grouter.api.matcher

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import my.itgungnir.grouter.api.Router
import my.itgungnir.grouter.api.dto.RouterRequest

/**
 * PathMatcher：Activity路由匹配器
 *
 * 如果发现target内容存在于路由表中，则表示匹配成功
 */
class RouteMapMatcher : BaseMatcher(priority = 4) {

    override fun matched(request: RouterRequest): Boolean {
        val url = request.target.toString()
        if (!url.startsWith("/")) {
            return false
        }
        return Router.instance.routeMap.containsKey(url)
    }

    override fun proceed(request: RouterRequest): Intent {

        val targetClazz = Router.instance.routeMap[request.target.toString()]

        val intent = when (val context = request.safeContext()) {
            is Context -> Intent(context, targetClazz)
            is Fragment -> Intent(context.context, targetClazz)
            else -> throw IllegalArgumentException("GRouter only supports navigation from Contexts or Fragments.")
        }

        return intent.apply {
            addFlags(request.flags)
            putExtras(request.extras)
        }
    }
}
