package test.itgungnir.grouter.sub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_another3.*

class AnotherFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_another3, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button.setOnClickListener {
            val intent = Intent().apply {
                putExtra("backKey", "value from another fragment7")
            }
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
