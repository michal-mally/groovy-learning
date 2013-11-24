package pl.softwaremind.ckjava.recommendation.groovy.training

import static java.util.concurrent.ThreadLocalRandom.current as random
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class OrderItemBuilder {

    static OrderItem orderItem(Map params = [:]) {
        new OrderItem(
                params.code ?: randomAlphanumeric(10),
                params.name ?: randomAlphanumeric(20),
                params.quantity ?: randomBigDecimal(100_00, 2),
                params.netPricePerPiece ?: randomBigDecimal(100_00, 2),
                params.vatRate ?: randomBigDecimal(1_00, 2)
        )
    }

    private static BigDecimal randomBigDecimal(long limit, int scale) {
        BigDecimal.valueOf(random().nextLong(limit), scale)
    }

}
