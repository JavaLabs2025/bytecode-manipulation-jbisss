package org.example.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class AbcVisitor extends ClassVisitor {

    private String className;
    private final Map<String, Integer> assignmentCount = new HashMap<>();

    public AbcVisitor() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(Opcodes.ASM9, mv) {

            @Override
            public void visitVarInsn(int opcode, int var) {
                if ((opcode >= Opcodes.ISTORE && opcode <= 78) || opcode == Opcodes.IINC) {
                    assignmentCount.merge(className, 1, Integer::sum);
                }
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                assignmentCount.merge(className, 1, Integer::sum);
                super.visitIincInsn(var, increment);
            }
        };
    }

    public Map<String, Integer> getAssignmentCount() {
        return assignmentCount;
    }
}
