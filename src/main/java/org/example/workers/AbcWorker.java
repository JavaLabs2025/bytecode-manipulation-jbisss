package org.example.workers;

import org.example.StatItemDto;
import org.example.visitor.AbcVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbcWorker extends BaseWorker {

    @Override
    public StatItemDto doTheJob(String pathToJar, ClassVisitor visitor) throws IOException {
        loadJar(pathToJar, visitor);
        return collectAbc(visitor);
    }

    private StatItemDto collectAbc(ClassVisitor visitor) {
        Map<String, Integer> assignmentCount = ((AbcVisitor) visitor).getAssignmentCount();
        double avgAssignments = assignmentCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        Map<String, Integer> branchCount = ((AbcVisitor) visitor).getBranchCount();
        double avgBranches = branchCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        Map<String, Integer> conditionCount = ((AbcVisitor) visitor).getConditionCount();
        double avgConditions = conditionCount.values()
                .stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        List<StatItemDto.Item> items = new ArrayList<>();
        items.add(new StatItemDto.Item("Average assignments count", Double.toString(avgAssignments)));
        items.add(new StatItemDto.Item("Average branches count", Double.toString(avgBranches)));
        items.add(new StatItemDto.Item("Average conditions count", Double.toString(avgConditions)));

        return new StatItemDto(MetricEnum.ABC_COUNT, items);
    }
}
