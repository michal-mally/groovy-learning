package pl.helenium.learning.groovy.gpars

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ThreadLocalRandom

import static groovyx.gpars.GParsPool.*
import static pl.helenium.learning.groovy.gpars.Time.time

@CompileStatic
@Slf4j
class FindMaxTest {

    private List<Integer> numbers = []

    private ForkJoinPool forkJoinPool = new ForkJoinPool()

    @BeforeClass
    void initNumbers() {
        time("initNumbers") {
            (10 ** 7).times {
                numbers << ThreadLocalRandom.current().nextInt()
            }
        }
    }

    @Test(invocationCount = 5)
    void naive() {
        time("naive") {
            println "Max number: ${FindMaxNaive.instance.max(numbers)}"
        }
    }

    @Test(invocationCount = 5)
    void forkJoin() {
        time("forkJoin") {
            def task = forkJoinPool.submit(new FindMaxTask(numbers))
            println "Max number: ${task.get()}"
        }
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    @Test(invocationCount = 5)
    void gParsForkJoin() {
        def max = time("gParsForkJoin") {
            withPool {
                runForkJoin(numbers) {
                    if (it.size() < 1_000_000) {
                        return FindMaxNaive.instance.max(it)
                    }
                    def middle = it.size().intdiv 2
                    forkOffChild(it[0..<middle])

                    Math.max(runChildDirectly(it[middle..-1]), getChildrenResults()[0])
                }
            }
        }

        println "Max number: ${max}"
    }

}
