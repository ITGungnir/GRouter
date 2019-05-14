package my.itgungnir.grouter.api.result

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * ProxyManager
 *
 * 用于startActivityForResult需求。思路：
 * 1. 在本地的Context或Fragment中创建一个ProxyFragment
 * 2. 从这个ProxyFragment中调用startActivityForResult()方法
 * 3. 通过(RxJava)Observable来监听返回的数据
 */
class ProxyManager(private val context: Any, private val intent: Intent) {

    private val tag = "proxy_fragment"

    private val manager: FragmentManager
        get() = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is Fragment -> context.childFragmentManager
            else -> throw IllegalStateException("Unsupported context type.")
        }

    private val proxyFragment: ProxyFragment
        get() {
            val fragment = manager.findFragmentByTag(tag) as? ProxyFragment
                ?: ProxyFragment()
            if (!fragment.isAdded) {
                manager.beginTransaction().add(fragment, tag).commitNow()
            }
            return fragment
        }

    fun getResult() = proxyFragment.dispatchStartActivityForResult(intent)
}