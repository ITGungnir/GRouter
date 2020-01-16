package my.itgungnir.grouter.api.interceptor

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-16
 */
abstract class BaseGlobalInterceptor(private val priority: Int) : Interceptor, Comparable<BaseGlobalInterceptor> {

    init {
        if (priority < 1 || priority > 9) {
            throw IllegalArgumentException("An interceptor's priority should be in [1, 9].")
        }
    }

    override fun compareTo(other: BaseGlobalInterceptor) = when {
        this == other -> 0
        this.priority < other.priority -> -1
        else -> 1
    }
}
