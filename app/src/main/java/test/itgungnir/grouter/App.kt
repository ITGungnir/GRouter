package test.itgungnir.grouter

import androidx.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}