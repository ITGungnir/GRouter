package test.itgungnir.grouter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app4.*
import my.itgungnir.grouter.api.Router
import my.itgungnir.grouter.api.result.ProxyResult
import test.itgungnir.grouter.common.SubAnotherActivity6

class AppFragment4 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_app4, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button.setOnClickListener {
            Router.instance.with(this)
                .target(SubAnotherActivity6)
                .goForResult(1)?.subscribe {
                    if (it.code == ProxyResult.ResultCode.RESULT_OK) {
                        it.extras.getString("backKey")?.let { str ->
                            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}