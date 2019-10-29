package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another4.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router

@Route("another4")
class AnotherActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another4)

        button.setOnClickListener {
            Router.instance.with(this)
                .target("main")
                .clearGo()
        }
    }
}