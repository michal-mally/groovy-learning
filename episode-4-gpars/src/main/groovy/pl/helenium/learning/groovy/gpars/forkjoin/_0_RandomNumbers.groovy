package pl.helenium.learning.groovy.gpars.forkjoin

import static java.util.concurrent.ThreadLocalRandom.current as random

static List<Integer> randomNumbers() {
    (0..<10_000_000).collect() { random().nextInt() }
}
