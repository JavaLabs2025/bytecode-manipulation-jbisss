package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

public class AverageFieldsCountWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor, PrintStream ps) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, Integer> fieldCount = ((ClassMapVisitor) visitor).getFieldCount();

        printAverageFieldsCount(fieldCount, ps);
    }

    private void printAverageFieldsCount(Map<String, Integer> fieldCount, PrintStream ps) {
        double avgFields = fieldCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0);

        ps.println("Average fields amount: " + avgFields);
    }
}
