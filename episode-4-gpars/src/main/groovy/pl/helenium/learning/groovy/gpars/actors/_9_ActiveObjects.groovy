package pl.helenium.learning.groovy.gpars.actors

import groovyx.gpars.activeobject.ActiveObject
import groovyx.gpars.activeobject.ActiveMethod

@ActiveObject
class Decryptor {

    @ActiveMethod
    def decrypt(String encryptedText) {
        return encryptedText.reverse()
    }

    @ActiveMethod(blocking = true)
    def decrypt(Integer encryptedNumber) {
        return -1 * encryptedNumber + 142
    }

}

final Decryptor decryptor = new Decryptor()
def part1 = decryptor.decrypt(' noitcA ni yvoorG')
def part2 = decryptor.decrypt(140)
def part3 = decryptor.decrypt('noitide dn')

print part1.get()
print part2
println part3.get()
