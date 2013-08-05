import static groovy.lang.Closure.DELEGATE_FIRST

class PropertyBuilder {

    private final props = new Properties()

    private final path = []

    def methodMissing(String name, args) {
        path.push(name)

        Closure cl = args[0].clone()
        cl.resolveStrategy = DELEGATE_FIRST
        cl.delegate = this
        cl()

        path.pop()
    }

    def propertyMissing(String name, value) {
        props[[*path, name].join('.')] = value;
    }

}
