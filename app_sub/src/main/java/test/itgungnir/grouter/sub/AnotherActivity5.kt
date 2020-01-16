package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.SubAnotherActivity5

@Route(SubAnotherActivity5)
class AnotherActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another5)

        val manager = supportFragmentManager

        manager.beginTransaction()
            .add(
                R.id.fragment,
                AnotherFragment5()
            )
            .commit()
    }
}
