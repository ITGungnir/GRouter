package test.itgungnir.grouter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import my.itgungnir.grouter.annotation.Route

@Route("account5")
class AccountActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account5)
    }
}