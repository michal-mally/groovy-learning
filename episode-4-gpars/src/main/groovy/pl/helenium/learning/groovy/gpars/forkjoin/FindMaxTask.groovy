package pl.helenium.learning.groovy.gpars.forkjoin

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

import static java.lang.Math.max
import static pl.helenium.learning.groovy.gpars.Time.time
import static pl.helenium.learning.groovy.gpars.forkjoin.RandomNumbers.randomNumbers

class MaxTask extends RecursiveTask<Integer> {

    private final List<Integer> numbers

    public MaxTask(List<Integer> numbers) {
        this.numbers = numbers
    }

    @Override
    protected Integer compute() {
        if (numbers.size() < 500_000) {
            return Naive.max(numbers)
        }

        def middle = numbers.size().intdiv 2
        def left = new MaxTask(numbers[0..<middle])
        left.fork()

        def right = new MaxTask(numbers[middle..-1])
        return max(right.compute(), left.join())
    }

}

def numbers = randomNumbers()

def forkJoinPool = new ForkJoinPool()
time id: 'forkJoin', {
    def task = forkJoinPool.submit(new MaxTask(numbers))
    println "Max number: ${task.get()}"
}

