package pl.helenium.learning.groovy.gpars

import org.apache.commons.lang3.time.StopWatch

final class Time {

    static void time(Map params, Closure<?> closure) {
        def watch = new StopWatch()
        (params.times?:5).times {
            watch.reset()
            watch.start()
            closure()
            watch.stop()
            println "Task ${params.id} took ${watch.time}ms to complete."
        }
    }

    private Time() {
        // intentionally left blank
    }

}
