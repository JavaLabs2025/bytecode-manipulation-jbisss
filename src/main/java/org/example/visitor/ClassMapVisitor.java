package org.example.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM8;

public class ClassMapVisitor extends ClassVisitor {

    private final Map<String, String> superMap = new HashMap<>();

    private final Map<String, Integer> fieldCount = new HashMap<>();

    private final Map<String, List<String>> methods = new HashMap<>();

    public ClassMapVisitor() {
        super(ASM8);
    }

    public Map<String, String> getSuperMap() {
        return superMap;
    }

    public Map<String, Integer> getFieldCount() {
        return fieldCount;
    }

    public Map<String, List<String>> getMethods() {
        return methods;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        superMap.put(name, superName);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        fieldCount.merge(name, 1, Integer::sum);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        methods.computeIfAbsent(name, k -> new ArrayList<>())
                .add(name + descriptor);

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
