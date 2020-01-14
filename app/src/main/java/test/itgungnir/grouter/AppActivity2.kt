package test.itgungnir.grouter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app2.*
import my.itgungnir.grouter.annotation.Route

@Route("/app/app2")
class AppActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app2)

        button.setOnClickListener {
            val intent = Intent().apply {
                putExtra("backKey", "value from app2")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}