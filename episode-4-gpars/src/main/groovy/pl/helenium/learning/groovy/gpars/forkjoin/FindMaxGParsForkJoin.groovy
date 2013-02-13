package pl.helenium.learning.groovy.gpars.forkjoin

import static groovyx.gpars.GParsPool.runForkJoin
import static groovyx.gpars.GParsPool.withPool
import static pl.helenium.learning.groovy.gpars.Time.time
import static pl.helenium.learning.groovy.gpars.forkjoin.RandomNumbers.randomNumbers

def numbers = randomNumbers()

time id: "gParsForkJoin", {
    withPool {
        def max = runForkJoin(numbers) {
            if (it.size() < 500_000) {
                return Naive.max(it)
            }
            def middle = it.size().intdiv 2
            forkOffChild(it[0..<middle])

            Math.max(runChildDirectly(it[middle..-1]), getChildrenResults().first())
        }

        println "Max: ${max}"
    }
}
