package pl.helenium.learning.groovy.gpars

import org.apache.commons.lang3.time.StopWatch

static void time(Map params, Closure<?> closure) {
    (params.times ?: 5).times {
        new StopWatch().with {
            start()
            closure()
            stop()
            println "Task ${params.id} took ${time}ms to complete."
        }
    }
}
