package test.itgungnir.grouter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.grouter.api.result.ProxyResult
import test.itgungnir.grouter.common.*
import test.itgungnir.grouter.common.router.interceptor.CertInterceptor
import test.itgungnir.grouter.common.router.interceptor.LoginInterceptor

@Route(AppMainActivity)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {

        // 跳转到本模块中的Activity，携带参数
        nav_in_module_activity.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity1)
                .addParam("key1", "value1")
                .addParam("key2", 222)
                .go()
        }

        // 跳转到其他模块中的Activity，携带参数
        nav_other_module_activity.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity1)
                .addParam("key1", "value1")
                .go()
        }

        // 跳转到本模块中的Activity for result
        nav_in_module_activity_for_result.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity2)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str -> toast(str) }
                    }
                }
        }

        // 跳转到其他模块中的Activity for result
        nav_other_module_activity_for_result.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity2)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str -> toast(str) }
                    }
                }
        }

        // 验证匹配失败回调 - go
        nav_not_matched_callback.setOnClickListener {
            Router.instance.with(this)
                .target("/ghost_grout/ghost_page")
                .go { toast("/ghost_grout/ghost_page页面不存在") }
        }

        // 验证匹配失败回调 - goForResult
        nav_not_matched_no_callback.setOnClickListener {
            Router.instance.with(this)
                .target("/ghost_grout/ghost_page2")
                .goForResult(2) { toast("/ghost_grout/ghost_page2页面不存在") }
        }

        // 验证addFlag和getIntent方法
        nav_add_flag_get_intent.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity3)
                .go()
        }

        // 验证clearGo方法
        nav_clear_go.setOnClickListener {
            Router.instance.with(this)
                .target(SubAnotherActivity1)
                .go()
        }

        // 添加局部拦截器 - 拦截通过
        nav_local_interceptor_pass.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity4)
                .addInterceptor(LoginInterceptor { toast("用户尚未登录，不能跳转到account4") })
                .go()
        }

        // 添加局部拦截器 - 被拦截
        nav_local_interceptor_intercepted.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity5)
                .addInterceptor(CertInterceptor { toast("用户尚未通过实名认证，不能跳转到account5") })
                .go()
        }

        // Activity1 -> Fragment1 -> Activity1
        nav_activity_fragment_activity.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity3)
                .go()
        }

        // Fragment1 -> Activity1 -> Fragment1
        nav_fragment_activity_fragment.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity4)
                .go()
        }

        // Fragment1 -> Fragment2 -> Fragment1
        nav_fragment_fragment_fragment.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity5)
                .go()
        }

        // 使用DeepLinkMatcher：打开系统浏览器
        nav_open_web_browser.setOnClickListener {
            Router.instance.with(this)
                .target("https://www.baidu.com/")
                .go()
        }

        // 使用DeepLinkMatcher：打开TestDeepLink应用
        nav_open_custom_app.setOnClickListener {
            Router.instance.with(this)
                .target("deeplink://test.deeplink.com?param=hello,deepLink")
                .go { toast("当前设备上没有安装TestDeepLink应用！") }
        }

        // 使用自定义Matcher：拨打电话
        nav_custom_matcher.setOnClickListener {
            Router.instance.with(this)
                .target("tel:88888888")
                .go()
        }
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
