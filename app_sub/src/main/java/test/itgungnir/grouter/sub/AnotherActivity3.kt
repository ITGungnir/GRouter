package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.SubAnotherActivity3

@Route(SubAnotherActivity3)
class AnotherActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another3)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment,
                AnotherFragment3()
            )
            .commit()
    }
}
