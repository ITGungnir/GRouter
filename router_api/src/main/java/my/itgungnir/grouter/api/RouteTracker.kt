package my.itgungnir.grouter.api

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

class RouteTracker {

    // 任务栈
    private val stack = Stack<Activity>()

    companion object {
        val instance by lazy { RouteTracker() }
    }

    /**
     * 在Application初始化时调用
     */
    fun init(context: Application) {
        context.registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
    }

    /**
     * singleTask go
     */
    fun clearGo(target: Class<*>, callback: () -> Unit) {
        if (!exists(target)) {
            callback.invoke()
            return
        }
        while (true) {
            val activity = stack.peek()
            if (activity.javaClass == target) {
                break
            } else {
                stack.pop().finish()
            }
        }
    }

    /**
     * 判断Activity是否存在于任务栈中
     */
    private fun exists(target: Class<*>): Boolean {
        for (index in stack.size - 1 downTo 0) {
            if (stack[index].javaClass == target) {
                return true
            }
        }
        return false
    }

    /**
     * 监听所有Activity的生命周期
     *
     * 在Activity#onCreate()时将其入栈
     * 在Activity#onDestroy()时将其从栈中删除
     */
    inner class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let { stack.push(it) }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

        override fun onActivityStarted(activity: Activity?) {}

        override fun onActivityResumed(activity: Activity?) {}

        override fun onActivityPaused(activity: Activity?) {}

        override fun onActivityStopped(activity: Activity?) {}

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.let { stack.remove(it) }
        }
    }
}
