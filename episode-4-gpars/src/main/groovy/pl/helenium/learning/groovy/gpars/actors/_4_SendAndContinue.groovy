package pl.helenium.learning.groovy.gpars.actors

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

friend.sendAndContinue 'I need money!', { money -> println "I got ${money}\$" }
println 'I can continue while my friend is collecting money for me'

SECONDS.sleep 10
