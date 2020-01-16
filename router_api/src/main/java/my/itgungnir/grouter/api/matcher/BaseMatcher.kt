package my.itgungnir.grouter.api.matcher

import android.content.Intent
import my.itgungnir.grouter.api.dto.RouterRequest

/**
 * Matcher：路由匹配器
 *
 * 用于根据路由请求中的数据判断生成不同类型的Intent对象
 * Matcher具有优先级，优先级高的先匹配
 * Matcher的优先级为1~9，其中1最高，最先匹配
 */
abstract class BaseMatcher(private val priority: Int = 9) : Comparable<BaseMatcher> {

    init {
        // 排除优先级不合法的Matcher
        if (priority < 1 || priority > 9) {
            throw IllegalArgumentException("A matcher's priority should be in [1, 9].")
        }
    }

    /**
     * 判断当前Matcher匹配请求是否成功
     */
    abstract fun matched(request: RouterRequest): Boolean

    /**
     * 如果匹配成功，执行此方法生成Intent对象
     */
    abstract fun proceed(request: RouterRequest): Intent

    override fun compareTo(other: BaseMatcher) = when {
        this == other -> 0
        this.priority < other.priority -> -1
        else -> 1
    }
}
