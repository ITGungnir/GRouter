package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route

@Route("app5")
class AppActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app5)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, AppFragment5())
            .commit()
    }
}