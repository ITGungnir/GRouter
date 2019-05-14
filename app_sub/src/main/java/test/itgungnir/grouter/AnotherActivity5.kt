package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route

@Route("another5")
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