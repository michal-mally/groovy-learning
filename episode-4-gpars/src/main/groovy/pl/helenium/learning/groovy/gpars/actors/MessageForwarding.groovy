package pl.helenium.learning.groovy.gpars.actors

import static groovyx.gpars.actor.Actors.actor

def decryptor = actor {
    react {message ->
        reply message.reverse()
//        sender.send message.reverse()    //An alternative way to send replies
    }
}

def console = actor {  //This actor will print out decrypted messages, since the replies are forwarded to it
    react {
        println 'Decrypted message: ' + it
    }
}

decryptor.send 'lellarap si yvoorG', console  //Specify an actor to send replies to
console.join()
