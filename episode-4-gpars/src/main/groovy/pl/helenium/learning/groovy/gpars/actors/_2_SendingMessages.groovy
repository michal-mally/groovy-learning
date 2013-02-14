package pl.helenium.learning.groovy.gpars.actors

import static groovyx.gpars.actor.Actors.actor

def passiveActor = actor {
    loop {
        react { msg -> println "Received: $msg"; }
    }
}

passiveActor.send 'Message 1'
passiveActor << 'Message 2'    //using the << operator
passiveActor 'Message 3'       //using the implicit call() method

passiveActor.join()
