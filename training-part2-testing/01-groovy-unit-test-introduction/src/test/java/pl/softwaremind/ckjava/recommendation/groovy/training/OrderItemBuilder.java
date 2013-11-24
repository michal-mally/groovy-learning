package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class OrderItemBuilder {

    private String code = randomAlphanumeric(10);

    private String name = randomAlphanumeric(20);

    private BigDecimal quantity = randomBigDecimal(100_00, 2);

    private BigDecimal netPricePerPiece = randomBigDecimal(100_00, 2);

    private BigDecimal vatRate = randomBigDecimal(1_00, 2);

    public static OrderItemBuilder orderItem() {
        return new OrderItemBuilder();
    }

    public OrderItemBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public OrderItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OrderItemBuilder withQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItemBuilder withNetPricePerPiece(BigDecimal netPricePerPiece) {
        this.netPricePerPiece = netPricePerPiece;
        return this;
    }

    public OrderItemBuilder withVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    private static BigDecimal randomBigDecimal(long limit, int scale) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(limit), scale);
    }

    public OrderItem build() {
        return new OrderItem(code, name, quantity, netPricePerPiece, vatRate);
    }

    private OrderItemBuilder() {
        // intentionally left blank
    }

}
