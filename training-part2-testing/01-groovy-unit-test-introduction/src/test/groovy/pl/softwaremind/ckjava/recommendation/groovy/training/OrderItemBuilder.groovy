package pl.softwaremind.ckjava.recommendation.groovy.training

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class OrderItemBuilder {

    static OrderItem orderItem(Map params = [:]) {
        new OrderItem(
                params.code             ?: randomAlphanumeric(10),
                params.name             ?: randomAlphanumeric(20),
                params.quantity         ?: BigDecimal.randomBetween(0.00, 100.00),
                params.netPricePerPiece ?: BigDecimal.randomBetween(0.00, 100.00),
                params.vatRate          ?: BigDecimal.randomBetween(0.00, 1.00)
        )
    }

}
