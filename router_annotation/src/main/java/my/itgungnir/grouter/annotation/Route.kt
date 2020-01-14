package my.itgungnir.grouter.annotation

/**
 * @Route 注解
 *
 * 在Activity上添加该注解，即可将该Activity标记为一个路由节点
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Route(val path: String)
