package test.itgungnir.grouter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another6.*
import my.itgungnir.grouter.annotation.Route

@Route("another6")
class AnotherActivity6 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another6)

        button.setOnClickListener {
            val intent = Intent().apply {
                putExtra("backKey", "value from another activity 6")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}