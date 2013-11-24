package pl.softwaremind.ckjava.recommendation.groovy.training

import static java.util.concurrent.ThreadLocalRandom.current as random
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.apache.commons.lang3.text.WordUtils.uncapitalize

class OrderItemBuilder {

    private String code = randomAlphanumeric(10)

    private String name = randomAlphanumeric(20)

    private BigDecimal quantity = randomBigDecimal(100_00, 2)

    private BigDecimal netPricePerPiece = randomBigDecimal(100_00, 2)

    private BigDecimal vatRate = randomBigDecimal(1_00, 2)

    private OrderItem builtInstance

    static OrderItemBuilder orderItem() {
        new OrderItemBuilder()
    }

    def methodMissing(String name, args) {
        if (!name.startsWith('with')) {
            throw new MissingMethodException(name, this.class, args)
        }

        if (args.size() != 1) {
            throw new MissingMethodException(name, this.class, args)
        }

        setProperty(uncapitalize(name[4..-1]), args[0])
        this
    }

    OrderItem build() {
        if (!builtInstance) {
            builtInstance = new OrderItem(code, name, quantity, netPricePerPiece, vatRate)
        }

        builtInstance
    }

    Object asType(Class type) {
        if (type == OrderItem) {
            return build()
        }
    }

    private static BigDecimal randomBigDecimal(long limit, int scale) {
        BigDecimal.valueOf(random().nextLong(limit), scale)
    }

    private OrderItemBuilder() {
        // intentionally left blank
    }

}
