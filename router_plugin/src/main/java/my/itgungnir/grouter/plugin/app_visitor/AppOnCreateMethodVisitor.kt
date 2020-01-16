package my.itgungnir.grouter.plugin.app_visitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AppOnCreateMethodVisitor(
    mv: MethodVisitor,
    private val routeTables: Set<String>,
    private val matcherTables: Set<String>,
    private val interceptorTables: Set<String>
) : MethodVisitor(Opcodes.ASM6, mv) {

    override fun visitInsn(opcode: Int) {
        if (opcode == Opcodes.RETURN) {
            // RouteTables
            routeTables.groupBy { it.endsWith("DefaultRoute") && !it.endsWith("Route4DefaultRoute") }
                .forEach {
                    when (it.key) {
                        // RouteTables：将所有DefaultRoute直接注入到代码中，默认加载
                        true -> it.value.forEach { fileName -> generateCode(fileName) }
                        // RouteTables：将非DefaultRoute在Router中注册，在使用时再懒加载
                        else -> it.value.forEach { fileName -> registerAdditionalRouteName(fileName) }
                    }
                }
            // MatcherTables
            matcherTables.forEach { fileName -> generateCode(fileName) }
            // GlobalInterceptorTables
            interceptorTables.forEach { fileName -> generateCode(fileName) }
            // RouteTracker
            registerRouteTracker()
        }
        super.visitInsn(opcode)
    }

    private fun generateCode(fileName: String) {
        val path = "my/itgungnir/grouter/$fileName"
        mv.visitTypeInsn(Opcodes.NEW, path)
        mv.visitInsn(Opcodes.DUP)
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, path, "<init>", "()V", false)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, path, "register", "()V", false)
    }

    private fun registerAdditionalRouteName(fileName: String) {
        val clzPath = "my/itgungnir/grouter/api/Router"
        val cpnPath = "my/itgungnir/grouter/api/Router\$Companion"
        mv.visitFieldInsn(Opcodes.GETSTATIC, clzPath, "Companion", "L$cpnPath;")
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, cpnPath, "getInstance", "()L$clzPath;", false)
        mv.visitLdcInsn("my.itgungnir.grouter.$fileName")
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clzPath, "registerAdditionalRouteMap", "(Ljava/lang/String;)V", false)
    }

    private fun registerRouteTracker() {
        val clzPath = "my/itgungnir/grouter/api/RouteTracker"
        val cpnPath = "my/itgungnir/grouter/api/RouteTracker\$Companion"
        val appPath = "android/app/Application"
        mv.visitFieldInsn(Opcodes.GETSTATIC, clzPath, "Companion", "L$cpnPath;")
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, cpnPath, "getInstance", "()L$clzPath;", false)
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitTypeInsn(Opcodes.CHECKCAST, appPath)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clzPath, "init", "(L$appPath;)V", false)
    }
}
