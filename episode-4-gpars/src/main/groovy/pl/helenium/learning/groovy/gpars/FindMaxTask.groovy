package pl.helenium.learning.groovy.gpars
import groovy.transform.CompileStatic

import java.util.concurrent.RecursiveTask

@CompileStatic
class FindMaxTask extends RecursiveTask<Integer> {

    private final List<Integer> numbers

    public FindMaxTask(List<Integer> numbers) {
        this.numbers = numbers
    }

    @Override
    protected Integer compute() {
        if (numbers.size() < 1_000_000) {
            return FindMaxNaive.instance.max(numbers)
        }

        def middle = numbers.size().intdiv 2
        def left = new FindMaxTask(numbers[0..<middle])
        left.fork()

        def right = new FindMaxTask(numbers[middle..-1])
        return Math.max(right.compute(), left.join())
    }

}
