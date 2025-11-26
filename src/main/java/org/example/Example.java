package org.example;

import org.example.visitor.ClassPrinter;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Example {

    public static void main(String[] args) throws IOException {
        try (JarFile sampleJar = new JarFile("src/main/resources/sample.jar")) {
            Enumeration<JarEntry> enumeration = sampleJar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassPrinter cp = new ClassPrinter();
                    ClassReader cr = new ClassReader(sampleJar.getInputStream(entry));
                    cr.accept(cp, 0);
                }
            }
        }
    }
}
