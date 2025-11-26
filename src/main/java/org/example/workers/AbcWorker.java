package org.example.workers;

import org.example.visitor.AbcVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

public class AbcWorker extends BaseWorker {

    @Override
    public void doTheJob(String pathToJar, ClassVisitor visitor, PrintStream ps) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, Integer> assignmentCount = ((AbcVisitor) visitor).getAssignmentCount();

        printAbc(assignmentCount, ps);
    }

    private void printAbc(Map<String, Integer> assignmentCount, PrintStream ps) {
        double avgAssignments = assignmentCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        ps.println("Average assignments count: " + avgAssignments);
    }
}
