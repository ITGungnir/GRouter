package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another1.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router

@Route("/another/another1")
class AnotherActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another1)

        button.setOnClickListener {
            Router.instance.with(this)
                .target("another2")
                .go()
        }
    }
}