package pl.helenium.learning.groovy.gpars.actors

import static groovyx.gpars.actor.Actors.actor

def console = actor {
    react {
        println 'Result: ' + it
    }
}

def calculator = actor {
    react {a ->
        react {b ->
            console.send(a + b)
        }
    }
}

calculator.send 2
calculator.send 3

console.join()
