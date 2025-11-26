package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

abstract public class BaseWorker {

    abstract public void doTheJob(String pathToJar, ClassVisitor visitor) throws IOException;

    void loadJar(String jarPath, ClassVisitor visitor) throws IOException {
        try (InputStream in = new FileInputStream(jarPath);
             JarInputStream jar = new JarInputStream(in)) {

            JarEntry entry;
            while ((entry = jar.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class")) continue;

                ClassReader cr = new ClassReader(jar);
                cr.accept(visitor, 0);
            }
        }
    }
}
