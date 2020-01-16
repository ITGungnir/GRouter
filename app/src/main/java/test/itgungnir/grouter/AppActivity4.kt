package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.AppAppActivity4

@Route(AppAppActivity4)
class AppActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app4)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, AppFragment4())
            .commit()
    }
}
