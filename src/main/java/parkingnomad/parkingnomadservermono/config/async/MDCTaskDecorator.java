package parkingnomad.parkingnomadservermono.config.async;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class MDCTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(final Runnable runnable) {
        final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

        return () -> {
            MDC.setContextMap(copyOfContextMap);
            runnable.run();
        };
    }
}
