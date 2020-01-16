package my.itgungnir.grouter.compiler

import com.squareup.kotlinpoet.*
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
class RouteCompiler : BaseCompiler() {

    override fun getSupportedAnnotationTypes() = linkedSetOf(Route::class.java.name)

    override fun process(annos: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val routes = roundEnv.getElementsAnnotatedWith(Route::class.java)

        if (routes.isNotEmpty()) {
            generateRouteTables(routes)
        }

        return true
    }

    private fun generateRouteTables(routes: Set<Element>) {

        val legalRoutes = routes.map { it as TypeElement }
            .map { it.getAnnotation(Route::class.java).path to it.asClassName() }
            .filter { it.first.startsWith("/") }
            .groupBy { it.first.substring(1).indexOf("/") == -1 }

        // default group
        legalRoutes[true]?.let {
            generateGroupedRouteTable("G${uuid()}DefaultRoute", it)
        }

        // other groups
        legalRoutes[false]?.groupBy { it.first.split("/")[1] }?.map {
            generateGroupedRouteTable("G${uuid()}Route4${it.key}", it.value)
        }
    }

    private fun generateGroupedRouteTable(tableName: String, routePairs: List<Pair<String, ClassName>>) {
        if (routePairs.isNullOrEmpty()) {
            return
        }

        val registerFun = FunSpec.builder("register")

        routePairs.forEach {
            registerFun.addStatement("%T.instance.registerRoute(%S, %T::class.java)", router(), it.first, it.second)
        }

        val moduleRouteTableClazz = TypeSpec.classBuilder(tableName)
            .addFunction(registerFun.build())
            .build()

        FileSpec.builder(sourceDirectory, tableName)
            .addType(moduleRouteTableClazz)
            .build()
            .writeTo(File(targetRoot))
    }
}
