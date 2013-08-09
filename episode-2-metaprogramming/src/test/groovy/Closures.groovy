import groovy.transform.Canonical
import org.testng.annotations.Test

class Closures {

    @Test
    void 'closures are objects'() {
        def cl = { a, b ->
            a + b
        }
        // they take parameters
        // they return values

        // instances of groovy.lang.Closure
        assert cl instanceof Closure

        // various Closure's methods and properties can be accessed
        assert cl.parameterTypes.size() == 2

        // closures can be called
        assert cl.call(2, 4) == 2 + 4
        assert cl(2, 6) == 2 + 6

        // closures can be passed as arguments to other methods/closures
        println cl
    }

    @Test
    void 'closure default argument is "it"'() {
        def cl = { it * 2 }

        assert cl(5) == 5 * 2
    }

    @Test
    void 'there are a lot of ways to express yourself'() {
        def cl = { acc, val -> acc + val }

        assert [1, 2, 3].inject(0, cl) == 6
        assert [1, 2, 3].inject(0) { acc, val -> acc + val } == 6
    }

    @Test
    void 'making use of closures'() {
        assert (1..4).findAll { it % 2 == 0} == [2, 4]

        def findAll = { Collection c, Closure test ->
            def results = []
            for (el in c) {
                if (test(el)) {
                    results << el
                }
            }

            results
        }

        assert findAll(1..4) { it % 2 == 0} == [2, 4]
    }

    @Test
    void 'there is this, owner and delegate'() {
        def cl = {
            println "cl.this = ${this.class}"
            println "cl.owner = ${owner.class}"
            println "cl.delegate = ${delegate.class}"

            def cl2 = {
                println "cl2.this = ${this.class}"
                println "cl2.owner = ${owner.class}"
                println "cl2.delegate = ${delegate.class}"
            }

            cl2()
        }

        cl()
    }

    @Test
    void 'making use of delegate'() {
        def john = new Person()

        def with = { target, Closure cl ->
            Closure cloned = cl.clone()
            cloned.delegate = target
            cloned.resolveStrategy = Closure.DELEGATE_FIRST
            cloned()
        }

        assert ! john.name
        assert ! john.age

        with(john) {
            name = 'John'
            age = 17
        }

        assert john.name == 'John'
        assert john.age == 17

    }

    @Canonical
    class Person {
        String name
        int age
    }

    @Test
    void 'test of accumulator'() {
        def acc = { n ->
            { i -> n + i }
        }
        assert acc(5)(6) == 5 + 6

        def plus = { a, b -> a + b }
        def acc2 = { n -> plus.curry n }
        assert acc2(5)(6) == 5 + 6
    }

    @Test
    void 'there is more'() {
       assert 'Yes, there is!'
    }

}
