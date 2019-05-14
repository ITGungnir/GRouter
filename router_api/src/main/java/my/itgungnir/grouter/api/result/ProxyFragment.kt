package my.itgungnir.grouter.api.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * ProxyFragment
 *
 * 代理Fragment
 * 用于处理startActivityForResult()方法
 */
class ProxyFragment : Fragment() {

    private var subject: PublishSubject<ProxyResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> subject?.onNext(
                ProxyResult(
                    code = ProxyResult.ResultCode.RESULT_OK,
                    extras = data?.extras ?: Bundle()
                )
            )
            else -> subject?.onNext(
                ProxyResult(
                    code = ProxyResult.ResultCode.RESULT_CANCELED,
                    extras = Bundle()
                )
            )
        }
    }

    fun dispatchStartActivityForResult(intent: Intent): Observable<ProxyResult> {
        this.subject = PublishSubject.create()
        return subject!!.doOnSubscribe {
            startActivityForResult(intent, 1)
        }
    }
}