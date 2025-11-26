package org.example;

import org.example.visitor.AbcVisitor;
import org.example.visitor.ClassMapVisitor;
import org.example.workers.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LabMain {

    private static final String PATH_TO_JAR = "src/main/resources/sample.jar";

    public static void main(String[] args) throws IOException {
        boolean toFile;
        try {
            toFile = Boolean.parseBoolean(args[0]);
        } catch (Exception e) {
            toFile = false;
        }

        ClassMapVisitor classMapVisitor = new ClassMapVisitor();
        AbcVisitor abcVisitor = new AbcVisitor();

        PrintStream ps = null;
        try {
            OutputStream os;
            if (toFile) {
                os = new FileOutputStream("output.txt");
            } else {
                os = System.out;
            }

            ps = new PrintStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert ps != null;
        ps.println("-----------------------------------------------------");
        MaxDepthCounterWorker maxDepthCounterWorker = new MaxDepthCounterWorker();
        maxDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor, ps);

        ps.println("-----------------------------------------------------");

        AverageDepthCounterWorker averageDepthCounterWorker = new AverageDepthCounterWorker();
        averageDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor, ps);

        ps.println("-----------------------------------------------------");

        AverageOverridesCounterWorker averageOverridesCounterWorker = new AverageOverridesCounterWorker();
        averageOverridesCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor, ps);

        ps.println("-----------------------------------------------------");

        AverageFieldsCountWorker averageFieldsCountWorker = new AverageFieldsCountWorker();
        averageFieldsCountWorker.doTheJob(PATH_TO_JAR, classMapVisitor, ps);

        ps.println("-----------------------------------------------------");

        AbcWorker abcWorker = new AbcWorker();
        abcWorker.doTheJob(PATH_TO_JAR, abcVisitor, ps);
        ps.println("-----------------------------------------------------");

        if (toFile) {
            ps.close();
        }
    }
}
