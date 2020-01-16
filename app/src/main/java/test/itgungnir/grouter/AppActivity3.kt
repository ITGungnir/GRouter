package test.itgungnir.grouter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app3.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.grouter.api.result.ProxyResult
import test.itgungnir.grouter.common.AppAppActivity3
import test.itgungnir.grouter.common.SubAnotherActivity5

@Route(AppAppActivity3)
class AppActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app3)

        button.setOnClickListener {
            Router.instance.with(this)
                .target(SubAnotherActivity5)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str ->
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}
