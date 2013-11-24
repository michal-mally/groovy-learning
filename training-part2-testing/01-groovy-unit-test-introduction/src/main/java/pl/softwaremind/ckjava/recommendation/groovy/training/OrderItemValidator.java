package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;

import static org.apache.commons.lang3.Validate.*;

public class OrderItemValidator {

    public static void validate(OrderItem item) {
        try {
            notBlank(item.getCode(), "Order item's code mustn't be blank!");
            notBlank(item.getName(), "Order item's name mustn't be blank!");
            notNull(item.getQuantity(), "Order item's quantity mustn't be blank!");
            inclusiveBetween(new BigDecimal("0.00"), new BigDecimal(Long.MAX_VALUE), item.getNetPricePerPiece(), "Order item's net price per piece has to be non-negative!");
            inclusiveBetween(new BigDecimal("0.00"), new BigDecimal("1.00"), item.getVatRate(), "Order item's vat rate must be in [0.00, 1.00] range");
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new OrderException("Order item is not valid!", e);
        }
    }

    private OrderItemValidator() {
        // intentionally left blank
    }

}
