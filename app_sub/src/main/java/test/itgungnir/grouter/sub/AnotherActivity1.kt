package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.SubAnotherActivity1

@Route(SubAnotherActivity1)
class AnotherActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another1)

        val manager = supportFragmentManager

        manager.beginTransaction()
            .add(
                R.id.fragment,
                AnotherFragment1()
            )
            .commit()
    }
}
