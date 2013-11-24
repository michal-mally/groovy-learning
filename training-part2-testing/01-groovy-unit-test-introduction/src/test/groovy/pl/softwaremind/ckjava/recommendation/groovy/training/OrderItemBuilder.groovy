package pl.softwaremind.ckjava.recommendation.groovy.training

import static java.util.concurrent.ThreadLocalRandom.current as random
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class OrderItemBuilder {

    private String code = randomAlphanumeric(10)

    private String name = randomAlphanumeric(20)

    private BigDecimal quantity = randomBigDecimal(100_00, 2)

    private BigDecimal netPricePerPiece = randomBigDecimal(100_00, 2)

    private BigDecimal vatRate = randomBigDecimal(1_00, 2)

    static OrderItemBuilder orderItem() {
        new OrderItemBuilder()
    }

    OrderItemBuilder withCode(String code) {
        this.code = code
        this
    }

    OrderItemBuilder withName(String name) {
        this.name = name
        this
    }

    OrderItemBuilder withQuantity(BigDecimal quantity) {
        this.quantity = quantity
        this
    }

    OrderItemBuilder withNetPricePerPiece(BigDecimal netPricePerPiece) {
        this.netPricePerPiece = netPricePerPiece
        this
    }

    OrderItemBuilder withVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate
        this
    }


    OrderItem build() {
        new OrderItem(code, name, quantity, netPricePerPiece, vatRate)
    }

    private static BigDecimal randomBigDecimal(long limit, int scale) {
        BigDecimal.valueOf(random().nextLong(limit), scale)
    }

    private OrderItemBuilder() {
        // intentionally left blank
    }

}
