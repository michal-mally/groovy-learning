import groovy.xml.MarkupBuilder
import org.testng.annotations.Test

class Builders {

    @Test
    void 'xml builder'() {
        def builder = new MarkupBuilder()

        builder.customers {
            customer name: 'John', age: 17, {
                insurances {
                    insurance type: 'car', value: 10_000
                }
            }
            customer name: 'Mary', age: 18, {
                insurances {
                    3.times {
                        insurance type: 'car', value: it * 20_000
                        insurance type: 'house', value: 1_000_000

                    }
                }
            }
        }

        println builder
    }

    @Test
    void 'custom builder'() {
        def builder = new PropertyBuilder()
        builder.com {
            mach {
                dbg {
                    connectors {
                        vodafone {
                            uk {
                                automaticTransactionStart = 'maybe'
                            }
                        }
                        o2 {
                            uk {
                                startStep666Timeout = 666
                            }
                        }
                    }
                }
            }
        }

        assert builder.props['com.mach.dbg.connectors.vodafone.uk.automaticTransactionStart'] == 'maybe'
        assert builder.props['com.mach.dbg.connectors.o2.uk.startStep666Timeout'] == 666
    }

}
