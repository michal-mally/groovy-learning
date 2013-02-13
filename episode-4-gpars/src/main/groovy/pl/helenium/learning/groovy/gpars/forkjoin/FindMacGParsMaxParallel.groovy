package pl.helenium.learning.groovy.gpars.forkjoin

import static groovyx.gpars.GParsPool.withPool
import static pl.helenium.learning.groovy.gpars.Time.time
import static pl.helenium.learning.groovy.gpars.forkjoin.RandomNumbers.randomNumbers

def numbers = randomNumbers()

time id: "maxParallel", {
    withPool {
        println "Max number: ${numbers.maxParallel()}"
    }
}
