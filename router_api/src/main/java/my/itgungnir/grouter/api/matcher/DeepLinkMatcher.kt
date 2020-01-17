package my.itgungnir.grouter.api.matcher

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import my.itgungnir.grouter.api.dto.RouterRequest
import java.util.regex.Pattern

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-17
 */
class DeepLinkMatcher : BaseMatcher(priority = 6) {

    override fun matched(request: RouterRequest): Boolean {
        val target = request.target.toString().trim()
        if (target.isBlank()) {
            return false
        }
        val pattern = "^\\w+://.+(:\\d)?(/.*)*((\\?)(.+=.*)(&.+=.*)*)?\$"
        if (!Pattern.compile(pattern).matcher(target).matches()) {
            return false
        }
        val context = request.context?.get() ?: return false
        val packageManager = when (context) {
            is Context -> context.packageManager
            is Fragment -> context.context?.packageManager ?: return false
            else -> return false
        }
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(target)
        }
        return intent.resolveActivity(packageManager) != null
    }

    override fun proceed(request: RouterRequest): Intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(request.target.toString())
    }
}
