package org.example.workers;

import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.util.Map;

public class AverageFieldsCountWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, Integer> fieldCount = ((ClassMapVisitor) visitor).getFieldCount();

        printAverageFieldsCount(fieldCount);
    }

    private void printAverageFieldsCount(Map<String, Integer> fieldCount) {
        double avgFields = fieldCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0);

        System.out.println("Average fields amount: " + avgFields);
    }
}
