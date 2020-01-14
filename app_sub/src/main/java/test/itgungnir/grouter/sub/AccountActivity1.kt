package test.itgungnir.grouter.sub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_account1.*
import my.itgungnir.grouter.annotation.Route

@Route("/account/account1")
class AccountActivity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account1)

        param.text = intent.getStringExtra("key1") ?: "null"
    }
}