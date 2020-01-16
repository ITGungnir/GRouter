package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app1.*
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.AppAppActivity1

@Route(AppAppActivity1)
class AppActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app1)

        param1.text = intent.getStringExtra("key1") ?: "null"

        param2.text = intent.getIntExtra("key2", 0).toString()
    }
}
