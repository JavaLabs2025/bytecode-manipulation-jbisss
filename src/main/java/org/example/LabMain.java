package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.visitor.AbcVisitor;
import org.example.visitor.ClassMapVisitor;
import org.example.workers.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
        List<StatItemDto> resultStats = new ArrayList<>();

        MaxDepthCounterWorker maxDepthCounterWorker = new MaxDepthCounterWorker();
        StatItemDto maxDepthCounterWorkerStat = maxDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);
        resultStats.add(maxDepthCounterWorkerStat);

        AverageDepthCounterWorker averageDepthCounterWorker = new AverageDepthCounterWorker();
        StatItemDto averageDepthCounterWorkerStat = averageDepthCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);
        resultStats.add(averageDepthCounterWorkerStat);

        AverageOverridesCounterWorker averageOverridesCounterWorker = new AverageOverridesCounterWorker();
        StatItemDto averageOverridesCounterWorkerStat = averageOverridesCounterWorker.doTheJob(PATH_TO_JAR, classMapVisitor);
        resultStats.add(averageOverridesCounterWorkerStat);

        AverageFieldsCountWorker averageFieldsCountWorker = new AverageFieldsCountWorker();
        StatItemDto averageFieldsCountWorkerStat = averageFieldsCountWorker.doTheJob(PATH_TO_JAR, classMapVisitor);
        resultStats.add(averageFieldsCountWorkerStat);

        AbcWorker abcWorker = new AbcWorker();
        StatItemDto abcWorkerStat = abcWorker.doTheJob(PATH_TO_JAR, abcVisitor);
        resultStats.add(abcWorkerStat);

        printAsJson(resultStats, ps);

        if (toFile) {
            ps.close();
        }
    }

    private static void printAsJson(List<?> list, PrintStream out) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(list);
            out.println(json);
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
