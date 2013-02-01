class PropertyBuilder {

    Properties props = new Properties()

    private path = []

    def methodMissing(String name, args) {
        path.push(name)

        Closure cl = args[0].clone()
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.delegate = this
        cl()

        path.pop()
    }

    def propertyMissing(String name, value) {
        props[[*path, name].join('.')] = value;
    }

}
