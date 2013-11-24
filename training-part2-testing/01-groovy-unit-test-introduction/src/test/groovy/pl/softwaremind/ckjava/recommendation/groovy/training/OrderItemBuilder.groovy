package pl.softwaremind.ckjava.recommendation.groovy.training

import static java.util.concurrent.ThreadLocalRandom.current as random
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class OrderItemBuilder {

    String code = randomAlphanumeric(10)

    String name = randomAlphanumeric(20)

    BigDecimal quantity = randomBigDecimal(100_00, 2)

    BigDecimal netPricePerPiece = randomBigDecimal(100_00, 2)

    BigDecimal vatRate = randomBigDecimal(1_00, 2)

    private OrderItem builtInstance

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

}
