package my.itgungnir.grouter.plugin.app_visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AppClassVisitor(
    cv: ClassVisitor,
    private val routeTables: Set<String>,
    private val matcherTables: Set<String>,
    private val interceptorTables: Set<String>
) : ClassVisitor(Opcodes.ASM6, cv) {

    private var superName: String? = null

    private var className: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.superName = superName
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, desc, signature, exceptions)
        if (superName.isNullOrBlank() || className.isNullOrBlank()) {
            return mv
        }
        if (superName!!.endsWith("Application") && name == "onCreate") {
            return AppOnCreateMethodVisitor(mv, routeTables, matcherTables, interceptorTables)
        }
        return mv
    }
}
