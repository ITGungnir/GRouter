package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another3.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import test.itgungnir.grouter.common.SubAnotherActivity3
import test.itgungnir.grouter.common.SubAnotherActivity4

@Route(SubAnotherActivity3)
class AnotherActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another3)

        button.setOnClickListener {
            Router.instance.with(this)
                .target(SubAnotherActivity4)
                .go()
        }
    }
}