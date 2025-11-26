package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class AverageDepthCounterWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor, PrintStream ps) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, String> classMap = ((ClassMapVisitor) visitor).getSuperMap();
        Map<String, Integer> depths = computeDepths(classMap);

        printAverageDepth(depths, ps);
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

    private void printAverageDepth(Map<String, Integer> depths, PrintStream ps) {
        double average = depths.values().stream()
                .mapToDouble(i -> i)
                .average()
                .orElse(0);

        ps.println("Average depth: " + average);
    }
}
