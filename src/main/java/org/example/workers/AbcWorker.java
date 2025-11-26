package org.example.workers;

import org.example.visitor.AbcVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.util.Map;

public class AbcWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, Integer> assignmentCount = ((AbcVisitor) visitor).getAssignmentCount();

        printAbc(assignmentCount);
    }

    private void printAbc(Map<String, Integer> assignmentCount) {
        double avgAssignments = assignmentCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        System.out.println("Average assignments count: " + avgAssignments);
    }
}
