package pl.helenium.learning.groovy.gpars.actors

import groovyx.gpars.dataflow.Promise

import static groovyx.gpars.actor.Actors.actor
import static java.util.concurrent.TimeUnit.SECONDS

def friend = actor {
    loop {
        react {
            SECONDS.sleep 5
            reply 100
            terminate()
        }
    }
}

Promise loan = friend.sendAndPromise 'I need money!'
println 'I can continue while my friend is collecting money for me'
loan.whenBound {money -> println "I got ${money}\$"}  // asynchronous waiting for a reply
println "Received ${loan.get()}"                      // synchronous waiting for a reply

SECONDS.sleep 10
