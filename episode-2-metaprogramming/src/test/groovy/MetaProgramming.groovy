import groovy.time.TimeCategory
import org.codehaus.groovy.runtime.InvokerHelper
import org.testng.annotations.Test

import java.util.concurrent.ThreadLocalRandom

class MetaProgramming {

    @Test
    void thereAreNoSpecialGroovyClasses() {
        assert "123" instanceof String
        assert 1 instanceof Integer
        assert [1, 2, 3] instanceof ArrayList
    }

    @Test
    void thereIsNoMagic() {
        def classes = [String, Integer, List]

        classes.each { clazz ->
            def metaClass = clazz.metaClass

            println "${clazz.simpleName} metamethods:"
            metaClass.metaMethods.each { m ->
                println "  ${m.returnType.simpleName.padRight 12 } ${clazz.simpleName}.${m.name}(${m.parameterTypes.join ', ' })"
            }
            println "${clazz.simpleName} metaproperties:"
            clazz.metaClass.metaPropertyValues.each { p ->
                println "  ${p.type.simpleName} ${clazz.simpleName}.${p.name}"
            }
        }

        assert Integer.metaClass.invokeConstructor(10) == 10
    }

    @Test
    void allCallsAreMadeThroughMetaObjectProtocol() {
        assert "123 456".center(15) == InvokerHelper.invokeMethod("123 456", "center", [15])
    }

    // how Groovy and how Java calls the methods

    @Test
    void addingUsefulMethodsToClass() {
        Integer.metaClass.random << { -> ThreadLocalRandom.current().nextInt delegate }

        10.times {
            def number = 10
            def random = number.random()
            println "${number}.random() = ${random}"
            assert random in 0..<10
        }

        Number.metaClass {
            getMm = { delegate }
            getCm = { delegate * 10.mm }
            getM = { delegate * 100.cm }
        }

        assert 1.m + 20.cm - 8.mm == 1.192.m
    }

    @Test
    void methodMissingHook() {
        def dynamic = new DynamicClass()
        assert dynamic.thereIsNoSpoon1() == "Yes, there is a Spoon1!"
        assert dynamic.thereIsNoWar() == "Yes, there is a War!"
    }

    @Test
    void propertyMissingHook() {
        def dynamic = new DynamicClass()
        dynamic.prop_one = "SPOON"
        assert dynamic.eno_porp == "SPOON"
        assert ! dynamic.prop_one
    }

    // what I won't talk about
    //   GroovyObject.invokeMethod()
    //   GroovyObject.setProperty()
    //   GroovyObject.getProperty()

    @Test
    void replaceMetaclass() {
        assert new Adder().add(10, 10) == 100

        def newMetaClass = new DelegatingMetaClass(Adder) {

            @Override
            Object invokeMethod(Object object, String methodName, Object[] arguments){
                if (methodName != "add") {
                    return super.invokeMethod(methodName, arguments)
                }

                return arguments[0] + arguments[1]
            }

        }
        newMetaClass.initialize()

        def oldMetaClass = Adder.metaClass
        Adder.metaClass = newMetaClass
        assert new Adder().add(10, 10) == 20

        Adder.metaClass = oldMetaClass
        assert new Adder().add(10, 10) == 100
    }

    @Test
    void categories() {
        use(TimeCategory) {
            println 1.month.from.now + 1.days
            println 2.days.ago
        }
    }

}
