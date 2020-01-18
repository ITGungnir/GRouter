package test.itgungnir.grouter.sub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_another2.*
import my.itgungnir.grouter.annotation.Route
import test.itgungnir.grouter.common.SubAnotherActivity2

@Route(SubAnotherActivity2)
class AnotherActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another2)

        button.setOnClickListener {
            val intent = Intent().apply {
                putExtra("backKey", "value from another activity 6")
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
