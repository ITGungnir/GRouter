package my.itgungnir.grouter.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import my.itgungnir.grouter.plugin.app_visitor.AppClassVisitor
import org.apache.commons.compress.utils.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.regex.Pattern
import java.util.zip.ZipEntry

class RouterTransform : Transform() {

    private val routeTables = mutableSetOf<String>()
    private val matcherTables = mutableSetOf<String>()
    private val interceptorTables = mutableSetOf<String>()

    override fun getName() = "GRouter"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> =
        TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = true

    override fun transform(transformInvocation: TransformInvocation) {
        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider
        // 扫描之前先清空
        routeTables.clear()
        matcherTables.clear()
        interceptorTables.clear()
        // 第一次扫描，收集需要注入的类名
        inputs.forEach { input ->
            input.jarInputs.forEach { scanJarFile(it, outputProvider) }
            input.directoryInputs.forEach { scanDirFile(it, outputProvider) }
        }
        // 第二次扫描，将类注入到Application的onCreate()方法中
        inputs.forEach { input ->
            input.directoryInputs.forEach { insertCodeIntoDirFile(it, outputProvider) }
        }
    }

    private fun scanDirFile(dirInput: DirectoryInput, outputProvider: TransformOutputProvider) {
        if (dirInput.file.isDirectory) {
            dirInput.file.forEachFile { file ->
                val name = file.name
                if (checkJarFile(name)) {
                    saveFileNames(name)
                }
            }
        }
        val dest = outputProvider.getContentLocation(
            dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY
        )
        FileUtils.copyDirectory(dirInput.file, dest)
    }

    private fun scanJarFile(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement()
                val name = jarEntry.name
                val zipEntry = ZipEntry(name)
                val inputStream = jarFile.getInputStream(jarEntry)
                if (checkJarFile(name)) {
                    val subName = name.substring(name.lastIndexOf("/") + 1)
                    saveFileNames(subName)
                }
                jarOutputStream.putNextEntry(zipEntry)
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
                jarOutputStream.closeEntry()
                inputStream.close()
            }
            jarOutputStream.close()
            jarFile.close()
            val dest = outputProvider.getContentLocation(
                jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    private fun insertCodeIntoDirFile(dirInput: DirectoryInput, outputProvider: TransformOutputProvider) {
        if (dirInput.file.isDirectory) {
            dirInput.file.forEachFile { file ->
                if (checkDirFile(file.absolutePath, file.name)) {
                    val cr = ClassReader(file.readBytes())
                    val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                    val cv = AppClassVisitor(cw, routeTables, matcherTables, interceptorTables)
                    cr.accept(cv, ClassReader.EXPAND_FRAMES)
                    val code = cw.toByteArray()
                    val fos = FileOutputStream(file.absolutePath)
                    fos.write(code)
                    fos.close()
                }
            }
        }
        val dest = outputProvider.getContentLocation(
            dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY
        )
        FileUtils.copyDirectory(dirInput.file, dest)
    }

    private fun checkDirFile(absolutePath: String, fileName: String) =
        fileName.endsWith(".class") &&
                !fileName.contains("\$") &&
                !checkJarFile(fileName) &&
                !(absolutePath.contains("/intermediates/") || absolutePath.contains("\\intermediates\\"))

    private fun checkJarFile(fileName: String) =
        Pattern.compile("^(my/itgungnir/grouter/)?G[0-9A-Z]{32}(((GlobalInterceptor|Matcher)Table)|(DefaultRoute)|(Route4.*)).class\$")
            .matcher(fileName).matches()

    private fun saveFileNames(name: String) {
        when {
            name.contains("Route4") || name.endsWith("DefaultRoute.class") ->
                routeTables.add(name.substring(0, name.length - 6))
            name.endsWith("MatcherTable.class") ->
                matcherTables.add(name.substring(0, name.length - 6))
            name.endsWith("GlobalInterceptorTable.class") ->
                interceptorTables.add(name.substring(0, name.length - 6))
        }
    }
}
