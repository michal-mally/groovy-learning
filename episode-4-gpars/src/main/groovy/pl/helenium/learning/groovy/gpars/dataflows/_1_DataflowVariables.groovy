package pl.helenium.learning.groovy.gpars.dataflows

import groovyx.gpars.dataflow.DataflowVariable

import static groovyx.gpars.dataflow.Dataflow.task

def book = new DataflowVariable()
def searchPhrase = new DataflowVariable()

task {
    book << [
            title:'Groovy in Action 2nd edition   ',
            author:'Dierk Koenig',
            publisher:'Manning']
}

task {
    searchPhrase << '2nd'
}

book.title.trim().contains(searchPhrase).whenBound {println it}  // Asynchronous waiting

println book.title.trim().contains(searchPhrase).val  // Synchronous waiting
