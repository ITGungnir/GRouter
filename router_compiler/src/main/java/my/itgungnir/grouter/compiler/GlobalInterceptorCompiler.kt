package my.itgungnir.grouter.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import my.itgungnir.grouter.annotation.GlobalInterceptor
import java.io.File
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * 全局路由拦截器GlobalInterceptor的生成器
 *
 * GlobalInterceptor用于在每个路由请求发出时进行拦截
 */
class GlobalInterceptorCompiler : BaseProcessor() {

    override fun getSupportedAnnotationTypes() = linkedSetOf(GlobalInterceptor::class.java.name)

    override fun process(annos: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val interceptors = roundEnv.getElementsAnnotatedWith(GlobalInterceptor::class.java)

        if (interceptors.isNotEmpty()) {
            generateGlobalInterceptorTable(interceptors)
        }

        return true
    }

    private fun generateGlobalInterceptorTable(interceptors: Set<Element>) {
        val registerFun = FunSpec.builder("register")

        val moduleInterceptorTableClazzName = "A${uuid()}GlobalInterceptorTable"

        interceptors.map { it as TypeElement }
            .forEach {
                registerFun.addStatement(
                    "%T.instance.registerGlobalInterceptor(%T())",
                    router(),
                    it.asClassName()
                )
            }

        val moduleInterceptorTableClazz = TypeSpec.classBuilder(moduleInterceptorTableClazzName)
            .addFunction(registerFun.build())
            .build()

        FileSpec.builder(sourceDirectory, moduleInterceptorTableClazzName)
            .addType(moduleInterceptorTableClazz)
            .build()
            .writeTo(File(targetRoot))
    }
}