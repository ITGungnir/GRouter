package my.itgungnir.grouter.compiler

import com.squareup.kotlinpoet.ClassName
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * 代码生成器的父类
 *
 * 提供了代码生成器中的公共属性和方法
 */
abstract class BaseCompiler : AbstractProcessor() {

    // 生成文件的目录
    protected val sourceDirectory = "my.itgungnir.grouter"

    // kapt工具生成代码的根路径
    protected lateinit var targetRoot: String

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        // 初始化属性
        this.targetRoot = processingEnv.options["kapt.kotlin.generated"].orEmpty()
    }

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()!!

    /**
     * router_api项目下的Router类
     */
    protected fun router() = ClassName("my.itgungnir.grouter.api", "Router")

    /**
     * 生成唯一的UUID字符串
     */
    protected fun uuid() = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.CHINA)

    abstract override fun getSupportedAnnotationTypes(): MutableSet<String>

    abstract override fun process(annos: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean
}
