package org.example;

import org.example.visitor.AbcVisitor;
import org.example.visitor.ClassMapVisitor;
import org.example.workers.*;

import java.io.IOException;

public class LabMain {

    private static final String PATH_TO_JAR = "src/main/resources/sample.jar";

    public static void main(String[] args) throws IOException {
        ClassMapVisitor classMapVisitor = new ClassMapVisitor();
        AbcVisitor abcVisitor = new AbcVisitor();

        System.out.println("-----------------------------------------------------");
        MaxDepthCounterWorker maxDepthCounterWorker = new MaxDepthCounterWorker();
        maxDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);

        System.out.println("-----------------------------------------------------");

        AverageDepthCounterWorker averageDepthCounterWorker = new AverageDepthCounterWorker();
        averageDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);

        System.out.println("-----------------------------------------------------");

        AverageOverridesCounterWorker averageOverridesCounterWorker = new AverageOverridesCounterWorker();
        averageOverridesCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);

        System.out.println("-----------------------------------------------------");

        AverageFieldsCountWorker averageFieldsCountWorker = new AverageFieldsCountWorker();
        averageFieldsCountWorker.doTheJob(PATH_TO_JAR, classMapVisitor);

        System.out.println("-----------------------------------------------------");

        AbcWorker abcWorker = new AbcWorker();
        abcWorker.doTheJob(PATH_TO_JAR, abcVisitor);
        System.out.println("-----------------------------------------------------");
    }
}
