class DynamicClass {

    def dynamicProps = [:]

    def methodMissing(String name, args) {
        def matcher = name =~ /thereIsNo(\w+)/
        if (!matcher) {
            super.invokeMethod(name, args)
        }

        "Yes, there is a ${matcher[0][1]}!"
    }

    def propertyMissing(String name) {
        dynamicProps[name]
    }

    def propertyMissing(String name, value) {
        dynamicProps[name.reverse()] = value
    }

}
