package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageOverridesCounterWorker extends BaseWorker {

    private final Map<String, Integer> overriddenCount = new HashMap<>();

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor, PrintStream ps) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, List<String>> methods = ((ClassMapVisitor) visitor).getMethods();
        Map<String, String> superMap = ((ClassMapVisitor) visitor).getSuperMap();
        for (String cls : methods.keySet()) {
            int count = 0;
            String current = superMap.get(cls);

            while (current != null) {

                List<String> parentMethods = methods.get(current);
                if (parentMethods != null) {
                    for (String m : methods.get(cls)) {
                        if (parentMethods.contains(m)) {
                            count++;
                        }
                    }
                }

                current = superMap.get(current);
            }

            overriddenCount.put(cls, count);
        }
        printOverriddenCounts(ps);
    }

    private void printOverriddenCounts(PrintStream ps) {
        ps.println("Overrides counts:");
        for (String cls : overriddenCount.keySet()) {
            ps.println(cls + " - " + overriddenCount.get(cls));
        }
    }
}
