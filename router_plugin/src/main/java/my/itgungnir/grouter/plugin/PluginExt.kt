package my.itgungnir.grouter.plugin

import java.io.File

fun File.forEachFile(callback: (File) -> Unit) {
    if (!this.exists() || !this.isDirectory) {
        return
    }
    val children = this.listFiles()
    if (children.isNullOrEmpty()) {
        return
    }
    children.forEach { child ->
        if (child.isDirectory) {
            child.forEachFile { callback.invoke(it) }
        } else {
            callback.invoke(child)
        }
    }
}