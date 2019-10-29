package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another2.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router

@Route("another2")
class AnotherActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another2)

        button.setOnClickListener {
            Router.instance.with(this)
                .target("another3")
                .go()
        }
    }
}