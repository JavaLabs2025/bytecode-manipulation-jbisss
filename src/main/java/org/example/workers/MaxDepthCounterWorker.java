package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class MaxDepthCounterWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor, PrintStream ps) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, String> classMap = ((ClassMapVisitor) visitor).getSuperMap();
        Map<String, Integer> depths = computeDepths(classMap);

        printMaxDepth(depths, ps);
    }

    private Map<String, Integer> computeDepths(Map<String, String> classMap) {
        Map<String, Integer> result = new HashMap<>();
        for (String clazz : classMap.keySet()) {
            result.put(clazz, depthOf(clazz, classMap));
        }
        return result;
    }

    private int depthOf(String cls, Map<String, String> classMap) {
        int depth = 0;
        String current = cls;

        while (true) {
            String parent = classMap.get(current);
            if (parent == null) return depth;
            depth++;
            current = parent;
        }
    }

    private void printMaxDepth(Map<String, Integer> depths, PrintStream ps) {
        Map.Entry<String, Integer> maxEntry = depths.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (maxEntry != null) {
            ps.println("Class with max depth: " + maxEntry.getKey());
            ps.println("Max depth: " + maxEntry.getValue());
        }
    }
}
