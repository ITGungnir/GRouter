package test.itgungnir.grouter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.grouter.api.result.ProxyResult
import test.itgungnir.grouter.common.router.interceptor.CertInterceptor
import test.itgungnir.grouter.common.router.interceptor.LoginInterceptor

@Route("main")
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
                .target("app1")
                .addParam("key1", "value1")
                .addParam("key2", 222)
                .go()
        }

        // 跳转到其他模块中的Activity，携带参数
        button2.setOnClickListener {
            Router.instance.with(this)
                .target("account1")
                .addParam("key1", "value1")
                .go()
        }

        // 跳转到本模块中的Activity for result
        button3.setOnClickListener {
            Router.instance.with(this)
                .target("app2")
                .goForResult()?.subscribe {
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
                .target("account2")
                .goForResult()?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str ->
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        // 验证addFlag和getIntent方法
        button5.setOnClickListener {
            Router.instance.with(this)
                .target("account3")
                .go()
        }

        // 验证clearGo方法
        button6.setOnClickListener {
            Router.instance.with(this)
                .target("another1")
                .go()
        }

        // 添加登录拦截器
        button7.setOnClickListener {
            Router.instance.with(this)
                .target("account4")
                .addInterceptor(LoginInterceptor {
                    Toast.makeText(this, "用户尚未登录，不能跳转到account4", Toast.LENGTH_SHORT).show()
                })
                .go()
        }

        // 添加实名认证拦截器
        button8.setOnClickListener {
            Router.instance.with(this)
                .target("account5")
                .addInterceptor(CertInterceptor {
                    Toast.makeText(this, "用户尚未通过实名认证，不能跳转到account5", Toast.LENGTH_SHORT).show()
                })
                .go()
        }

        // Activity1 -> Fragment1 -> Activity1
        button9.setOnClickListener {
            Router.instance.with(this)
                .target("app3")
                .go()
        }

        // Fragment1 -> Activity1 -> Fragment1
        button10.setOnClickListener {
            Router.instance.with(this)
                .target("app4")
                .go()
        }

        // Fragment1 -> Fragment2 -> Fragment1
        button11.setOnClickListener {
            Router.instance.with(this)
                .target("app5")
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