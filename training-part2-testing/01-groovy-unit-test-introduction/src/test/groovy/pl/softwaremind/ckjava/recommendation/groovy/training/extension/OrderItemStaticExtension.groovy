package pl.softwaremind.ckjava.recommendation.groovy.training.extension
import pl.softwaremind.ckjava.recommendation.groovy.training.OrderItem

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class OrderItemStaticExtension {

    static OrderItem with(OrderItem selfType, Map params) {
        new OrderItem(
                params.code             ?: randomAlphanumeric(10),
                params.name             ?: randomAlphanumeric(20),
                params.quantity         ?: BigDecimal.randomBetween(0.00, 100.00),
                params.netPricePerPiece ?: BigDecimal.randomBetween(0.00, 100.00),
                params.vatRate          ?: BigDecimal.randomBetween(0.00, 1.00)
        )
    }

    static OrderItem withDefault(OrderItem selfType) {
        OrderItem.with([:])
    }

}
