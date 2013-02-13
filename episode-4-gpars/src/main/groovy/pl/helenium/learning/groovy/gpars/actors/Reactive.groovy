package pl.helenium.learning.groovy.gpars.actors

import static groovyx.gpars.actor.Actors.actor
import static groovyx.gpars.actor.Actors.reactor

def doubler = reactor {
    2 * it
}

def actor = actor {
    (1..10).each {
        doubler << it
    }
    int i = 0
    loop {
        i++
        if (i > 10) {
            stop()
        } else {
            react { message ->
                println "Double of $i = $message"
            }
        }
    }
}

actor.join()
doubler.stop()
doubler.join()
