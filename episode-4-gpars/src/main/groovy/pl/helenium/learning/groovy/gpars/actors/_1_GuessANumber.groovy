// example by Jordi Campos i Miralles, Departament de MatemĂ tica Aplicada i AnĂ lisi, MAiA Facultat de MatemĂ tiques, Universitat de Barcelona

package pl.helenium.learning.groovy.gpars.actors

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

class GameMaster extends DefaultActor {

    public static final REPLY_MESSAGES = [
            (-1): 'too small',
             (0): 'you win',
             (1): 'too large'
    ]

    int secretNum

    void afterStart() {
        secretNum = new Random().nextInt(10)
    }

    void act() {
        loop {
            react { int num ->
                reply REPLY_MESSAGES[num <=> secretNum]
                if (num == secretNum) {
                    terminate()
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
                    case 'too large':
                        println "$name: $myNum was too large";
                        break
                    case 'too small':
                        println "$name: $myNum was too small";
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
