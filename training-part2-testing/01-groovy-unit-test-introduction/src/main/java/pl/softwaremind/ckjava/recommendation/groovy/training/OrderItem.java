package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static pl.softwaremind.ckjava.recommendation.groovy.training.OrderItemValidator.validate;

public class OrderItem {

    private final String code;

    private final String name;

    private final BigDecimal quantity;

    private final BigDecimal netPricePerPiece;

    private final BigDecimal vatRate;

    public OrderItem(String code, String name, BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.netPricePerPiece = netPricePerPiece;
        this.vatRate = vatRate;

        validate(this);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getNetPricePerPiece() {
        return netPricePerPiece;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public BigDecimal getNetTotal() {
        return getNetPricePerPiece().multiply(getQuantity()).setScale(2, HALF_UP);
    }

    public BigDecimal getGrossTotal() {
        return getNetTotal().multiply(BigDecimal.ONE.add(getVatRate())).setScale(2, HALF_UP);
    }

}
