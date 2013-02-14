package pl.helenium.learning.groovy.gpars.dataflows
import groovyx.gpars.dataflow.DataflowBroadcast

def broadcastStream = new DataflowBroadcast()
def stream1 = broadcastStream.createReadChannel()
def stream2 = broadcastStream.createReadChannel()

broadcastStream << 'Message1'
broadcastStream << 'Message2'
broadcastStream << 'Message3'

assert stream1.val == stream2.val
assert stream1.val == stream2.val
assert stream1.val == stream2.val
