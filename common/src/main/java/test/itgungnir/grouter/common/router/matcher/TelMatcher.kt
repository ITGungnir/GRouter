package test.itgungnir.grouter.common.router.matcher

import android.content.Intent
import my.itgungnir.grouter.annotation.Matcher
import my.itgungnir.grouter.api.dto.RouterRequest
import my.itgungnir.grouter.api.matcher.BaseMatcher

@Matcher
class TelMatcher : BaseMatcher(priority = 3) {

    override fun matched(request: RouterRequest): Boolean =
        request.target.toString().startsWith("tel:")

    override fun proceed(request: RouterRequest): Intent =
        Intent(Intent.ACTION_DIAL, request.target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
}