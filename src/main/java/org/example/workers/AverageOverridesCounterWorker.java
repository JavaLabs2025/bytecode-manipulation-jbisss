package org.example.workers;

import org.example.StatItemDto;
import org.example.visitor.ClassMapVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageOverridesCounterWorker extends BaseWorker {

    private final Map<String, Integer> overriddenCount = new HashMap<>();

    @Override
    public StatItemDto doTheJob(String pathToJar, ClassVisitor visitor) throws IOException {
        loadJar(pathToJar, visitor);
        Map<String, List<String>> methods = ((ClassMapVisitor) visitor).getMethods();
        Map<String, String> superMap = ((ClassMapVisitor) visitor).getSuperMap();
        for (String cls : methods.keySet()) {
            int count = 0;
            String current = superMap.get(cls);

            while (current != null) {

                List<String> parentMethods = methods.get(current);
                if (parentMethods != null) {
                    for (String m : methods.get(cls)) {
                        if (parentMethods.contains(m)) {
                            count++;
                        }
                    }
                }

                current = superMap.get(current);
            }

            overriddenCount.put(cls, count);
        }
        return collectOverriddenCounts();
    }

    private StatItemDto collectOverriddenCounts() {
        List<StatItemDto.Item> items = new ArrayList<>();

        for (String cls : overriddenCount.keySet()) {
            items.add(new StatItemDto.Item(cls, Integer.toString(overriddenCount.get(cls))));
        }
        return new StatItemDto(MetricEnum.AVERAGE_OVERRIDES_COUNT, items);
    }
}
