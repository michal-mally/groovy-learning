package pl.helenium.learning.groovy.gpars

import org.apache.commons.lang3.time.StopWatch

static void time(Map params, Closure<?> closure) {
    (params.times ?: 5).times {
        def watch = new StopWatch()
        watch.start()
        closure()
        watch.stop()
        println "Task ${params.id} took ${watch.time}ms to complete."
    }
}
