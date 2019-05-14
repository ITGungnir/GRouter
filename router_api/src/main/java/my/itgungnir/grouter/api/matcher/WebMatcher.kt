package my.itgungnir.grouter.api.matcher

import android.content.Intent
import android.net.Uri
import my.itgungnir.grouter.api.dto.RouterRequest

/**
 * WebMatcher：系统浏览器匹配器
 *
 * 如果target以“http://”或“https://”开头，则表示匹配成功
 */
class WebMatcher : BaseMatcher(priority = 2) {

    override fun matched(request: RouterRequest): Boolean =
        request.target.toString().startsWith("http://") ||
                request.target.toString().startsWith("https://")

    override fun proceed(request: RouterRequest): Intent {

        val uri = Uri.parse(request.target.toString())

        return Intent().apply {
            action = Intent.ACTION_VIEW
            data = uri
        }
    }
}