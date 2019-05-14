package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route

@Route("account4")
class AccountActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account4)
    }
}