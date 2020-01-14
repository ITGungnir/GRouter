package my.itgungnir.grouter.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AppOnCreateMethodVisitor(
    mv: MethodVisitor,
    private val routeTables: Set<String>,
    private val matcherTables: Set<String>,
    private val interceptorTables: Set<String>
) : MethodVisitor(Opcodes.ASM6, mv) {

    private val clzPath = "my/itgungnir/grouter/api/RouteTracker"
    private val cpnPath = "my/itgungnir/grouter/api/RouteTracker\$Companion"
    private val appPath = "android/app/Application"

    override fun visitInsn(opcode: Int) {
        if (opcode == Opcodes.RETURN) {
            // RouteTables
            routeTables.forEach { fileName -> generateCode(fileName) }
            // MatcherTables
            matcherTables.forEach { fileName -> generateCode(fileName) }
            // GlobalInterceptorTables
            interceptorTables.forEach { fileName -> generateCode(fileName) }
            // RouteTracker
            mv.visitFieldInsn(Opcodes.GETSTATIC, clzPath, "Companion", "L$cpnPath;")
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, cpnPath, "getInstance", "()L$clzPath;", false)
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitTypeInsn(Opcodes.CHECKCAST, appPath)
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, clzPath, "init", "(L$appPath;)V", false)
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
}