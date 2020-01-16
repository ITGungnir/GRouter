package test.itgungnir.grouter.sub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another6.*
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.SubAnotherActivity6

@Route(SubAnotherActivity6)
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
