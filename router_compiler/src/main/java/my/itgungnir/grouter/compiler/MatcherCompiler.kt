package my.itgungnir.grouter.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import my.itgungnir.grouter.annotation.Matcher
import java.io.File
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * 各个模块的Matcher表生成器
 *
 * Matcher用于匹配路由字符串，并生成对应的Intent
 */
class MatcherCompiler : BaseCompiler() {

    override fun getSupportedAnnotationTypes() = linkedSetOf(Matcher::class.java.name)

    override fun process(annos: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val matchers = roundEnv.getElementsAnnotatedWith(Matcher::class.java)

        if (matchers.isNotEmpty()) {
            generateMatcherTable(matchers)
        }

        return true
    }

    private fun generateMatcherTable(matchers: Set<Element>) {

        val registerFun = FunSpec.builder("register")

        val moduleMatcherTableClazzName = "G${uuid()}MatcherTable"

        matchers.map { it as TypeElement }
            .forEach {
                registerFun.addStatement(
                    "%T.instance.registerMatcher(%T())",
                    router(),
                    it.asClassName()
                )
            }

        val moduleMatcherTableClazz = TypeSpec.classBuilder(moduleMatcherTableClazzName)
            .addFunction(registerFun.build())
            .build()

        FileSpec.builder(sourceDirectory, moduleMatcherTableClazzName)
            .addType(moduleMatcherTableClazz)
            .build()
            .writeTo(File(targetRoot))
    }
}
