package my.itgungnir.grouter.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        if (isApp) {
            project.extensions.getByType(AppExtension::class.java)
                .registerTransform(RouterTransform())
        }
    }
}