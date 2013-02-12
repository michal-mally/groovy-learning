package pl.helenium.learning.groovy.gpars

import groovy.util.logging.Slf4j
import org.apache.commons.lang3.time.StopWatch

@Slf4j
final class Time {

    static <T> T time(String id, Closure<T> closure) {
        def watch = new StopWatch()
        watch.start()
        def clReturn = closure()
        watch.stop()
        log.info("Task ${id} took ${watch.time}ms to complete.")
        return clReturn
    }

    private Time() {
        // intentionally left blank
    }

}
