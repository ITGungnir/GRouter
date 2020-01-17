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
        button1.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity1)
                .addParam("key1", "value1")
                .addParam("key2", 222)
                .go()
        }

        // 跳转到其他模块中的Activity，携带参数
        button2.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity1)
                .addParam("key1", "value1")
                .go()
        }

        // 跳转到本模块中的Activity for result
        button3.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity2)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str ->
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        // 跳转到其他模块中的Activity for result
        button4.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity2)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str ->
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        // 验证匹配失败回调 - 设置了回调方法
        button14.setOnClickListener {
            Router.instance.with(this)
                .target("/ghost_grout/ghost_page")
                .go {
                    Toast.makeText(this, "/ghost_grout/ghost_page页面不存在", Toast.LENGTH_SHORT).show()
                }
        }

        // 验证匹配失败回调 - 没有设置回调方法
        button15.setOnClickListener {
            Router.instance.with(this)
                .target("/ghost_grout/ghost_page2")
                .goForResult(2) {
                    Toast.makeText(this, "/ghost_grout/ghost_page2页面不存在", Toast.LENGTH_SHORT).show()
                }
        }

        // 验证addFlag和getIntent方法
        button5.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity3)
                .go()
        }

        // 验证clearGo方法
        button6.setOnClickListener {
            Router.instance.with(this)
                .target(SubAnotherActivity1)
                .go()
        }

        // 添加登录拦截器
        button7.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity4)
                .addInterceptor(LoginInterceptor {
                    Toast.makeText(this, "用户尚未登录，不能跳转到account4", Toast.LENGTH_SHORT).show()
                })
                .go()
        }

        // 添加实名认证拦截器
        button8.setOnClickListener {
            Router.instance.with(this)
                .target(SubAccountActivity5)
                .addInterceptor(CertInterceptor {
                    Toast.makeText(this, "用户尚未通过实名认证，不能跳转到account5", Toast.LENGTH_SHORT).show()
                })
                .go()
        }

        // Activity1 -> Fragment1 -> Activity1
        button9.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity3)
                .go()
        }

        // Fragment1 -> Activity1 -> Fragment1
        button10.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity4)
                .go()
        }

        // Fragment1 -> Fragment2 -> Fragment1
        button11.setOnClickListener {
            Router.instance.with(this)
                .target(AppAppActivity5)
                .go()
        }

        // 使用系统Matcher：打开网页
        button12.setOnClickListener {
            Router.instance.with(this)
                .target("https://www.baidu.com/")
                .go()
        }

        // 使用自定义Matcher：拨打电话
        button13.setOnClickListener {
            Router.instance.with(this)
                .target("tel:88888888")
                .go()
        }
    }
}
