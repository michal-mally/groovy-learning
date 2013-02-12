package pl.helenium.learning.groovy.gpars

import groovy.transform.CompileStatic

@Singleton
@CompileStatic
class FindMaxNaive {

    Integer max(List<Integer> numbers) {
        def max = Integer.MIN_VALUE
        numbers.each { Integer number ->
            if (number > max) {
                max = number
            }
        }
        max
    }

}
