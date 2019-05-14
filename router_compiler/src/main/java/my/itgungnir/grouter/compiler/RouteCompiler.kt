package my.itgungnir.grouter.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import my.itgungnir.grouter.annotation.Route
import java.io.File
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * 各个模块的路由表生成类
 *
 * 示例：
 * class AppRouteTable : RouteTable {
 *   override fun register() {
 *     Router.instance.registerRoute("app1", AppActivity1::class.java)
 *     Router.instance.registerRoute("main", MainActivity::class.java)
 *   }
 * }
 */
class RouteCompiler : BaseProcessor() {

    override fun getSupportedAnnotationTypes() = linkedSetOf(Route::class.java.name)

    override fun process(annos: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val routes = roundEnv.getElementsAnnotatedWith(Route::class.java)

        if (routes.isNotEmpty()) {
            generateRouteTable(routes)
        }

        return true
    }

    private fun generateRouteTable(routes: Set<Element>) {

        val registerFun = FunSpec.builder("register")

        val moduleRouteTableClazzName = "A${uuid()}RouteTable"

        routes.map { it as TypeElement }
            .forEach {
                registerFun.addStatement(
                    "%T.instance.registerRoute(%S, %T::class.java)",
                    router(),
                    it.getAnnotation(Route::class.java).path,
                    it.asClassName()
                )
            }

        val moduleRouteTableClazz = TypeSpec.classBuilder(moduleRouteTableClazzName)
            .addFunction(registerFun.build())
            .build()

        FileSpec.builder(sourceDirectory, moduleRouteTableClazzName)
            .addType(moduleRouteTableClazz)
            .build()
            .writeTo(File(targetRoot))
    }
}