package org.example.workers;

import org.example.StatItemDto;
import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class AverageFieldsCountWorker extends BaseWorker {

    @Override
    public StatItemDto doTheJob(String pathToJar, ClassVisitor visitor) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, Integer> fieldCount = ((ClassMapVisitor) visitor).getFieldCount();

        return collectAverageFieldsCount(fieldCount);
    }

    private StatItemDto collectAverageFieldsCount(Map<String, Integer> fieldCount) {
        double avgFields = fieldCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0);

        return new StatItemDto(MetricEnum.AVERAGE_FIELDS_COUNT, List.of(new StatItemDto.Item("Average fields amount", Double.toString(avgFields))));
    }
}
