package pl.helenium.learning.groovy.gpars.actors

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

class GameMaster extends DefaultActor {

    int secretNum

    void afterStart() {
        secretNum = new Random().nextInt(10)
    }

    void act() {
        loop {
            react { int num ->
                switch (num <=> secretNum) {
                    case -1: reply 'too small'; break;
                    case 0: reply 'you win'; terminate(); break;
                    case 1: reply 'too large'; break;
                }
            }
        }
    }

}

class Player extends DefaultActor {

    String name

    Actor server

    int myNum

    void act() {
        loop {
            myNum = new Random().nextInt(10)
            server << myNum
            react {
                switch (it) {
                    case ['too large', 'too small']:
                        println "$name: $myNum was ${it}";
                        break
                    case 'you win':
                        println "$name: I won $myNum";
                        terminate();
                        break
                }
            }
        }
    }

}

def master = new GameMaster().start()
def player = new Player(name: 'Player', server: master).start()

//this forces main thread to live until both actors stop
[master, player]*.join()
