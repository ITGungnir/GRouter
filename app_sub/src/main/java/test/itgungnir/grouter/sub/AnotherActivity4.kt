package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another4.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import test.itgungnir.grouter.common.AppMainActivity
import test.itgungnir.grouter.common.SubAnotherActivity4

@Route(SubAnotherActivity4)
class AnotherActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another4)

        button.setOnClickListener {
            Router.instance.with(this)
                .target(AppMainActivity)
                .clearGo()
        }
    }
}
