package pl.helenium.learning.groovy.gpars.forkjoin

import java.util.concurrent.ThreadLocalRandom

final class RandomNumbers {

    static List<Integer> randomNumbers() {
        (0..<10_000_000).collect() { ThreadLocalRandom.current().nextInt() }
    }

    private RandomNumbers() {
        // intentionally left blank
    }

}
