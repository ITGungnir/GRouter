package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route

@Route("another7")
class AnotherActivity7 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another7)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment,
                AnotherFragment7()
            )
            .commit()
    }
}