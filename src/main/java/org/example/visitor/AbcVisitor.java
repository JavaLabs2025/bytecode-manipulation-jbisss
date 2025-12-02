package org.example.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class AbcVisitor extends ClassVisitor {

    private String className;

    private final Map<String, Integer> assignmentCount = new HashMap<>();
    private final Map<String, Integer> branchCount = new HashMap<>();
    private final Map<String, Integer> conditionCount = new HashMap<>();

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

            // ------------------------------
            //  B — Branches
            // ------------------------------
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                branchCount.merge(className, 1, Integer::sum);
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                if (opcode == Opcodes.NEW) {
                    branchCount.merge(className, 1, Integer::sum);
                }
                super.visitTypeInsn(opcode, type);
            }


            @Override
            public void visitJumpInsn(int opcode, Label label) {
                if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
                    conditionCount.merge(className, 1, Integer::sum);
                }

                if (opcode == Opcodes.GOTO) {
                    conditionCount.merge(className, 1, Integer::sum);
                }

                super.visitJumpInsn(opcode, label);
            }

            @Override
            public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
                conditionCount.merge(className, 1, Integer::sum);
                super.visitLookupSwitchInsn(dflt, keys, labels);
            }

            @Override
            public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
                conditionCount.merge(className, 1, Integer::sum);
                super.visitTableSwitchInsn(min, max, dflt, labels);
            }

            @Override
            public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
                conditionCount.merge(className, 1, Integer::sum);
                super.visitTryCatchBlock(start, end, handler, type);
            }
        };
    }

    public Map<String, Integer> getAssignmentCount() {
        return assignmentCount;
    }

    public Map<String, Integer> getBranchCount() {
        return branchCount;
    }

    public Map<String, Integer> getConditionCount() {
        return conditionCount;
    }
}
