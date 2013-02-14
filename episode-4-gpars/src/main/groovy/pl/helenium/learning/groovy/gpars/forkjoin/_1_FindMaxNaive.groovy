package pl.helenium.learning.groovy.gpars.forkjoin

import static pl.helenium.learning.groovy.gpars.Time.time
import static pl.helenium.learning.groovy.gpars.forkjoin._0_RandomNumbers.randomNumbers

class Naive {

    static Integer max(List<Integer> numbers) {
        def max = Integer.MIN_VALUE
        numbers.each { Integer number ->
            if (number > max) {
                max = number
            }
        }
        max
    }

}

def numbers = randomNumbers()
time id: 'naive', {
    println Naive.max(numbers)
}
