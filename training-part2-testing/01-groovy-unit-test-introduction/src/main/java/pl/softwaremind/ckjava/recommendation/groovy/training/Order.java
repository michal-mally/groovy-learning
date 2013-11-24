package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {

    private final String number;

    private final Map<String, OrderItem> items = new LinkedHashMap<>();

    private boolean closed = false;

    public Order(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public Order addItem(OrderItem item) {
        if (isClosed()) {
            throw new OrderException("Cannot add items to already closed Order!");
        }

        if (getItemByCode(item.getCode()) != null) {
            throw new OrderException("Cannot add two items with the same code to the same Order!");
        }

        this.items.put(item.getCode(), item);
        return this;
    }

    public Collection<OrderItem> getItems() {
        return this.items.values();
    }

    public OrderItem getItemByCode(String code) {
        return this.items.get(code);
    }

    public Order close() {
        if (isClosed()) {
            return this;
        }

        if (this.items.isEmpty()) {
            throw new OrderException("Cannot close order with no items on it!");
        }

        this.closed = true;
        return this;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public BigDecimal getNetTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : this.items.values()) {
            total = total.add(item.getNetTotal());
        }

        return total;
    }

    public BigDecimal getGrossTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : this.items.values()) {
            total = total.add(item.getGrossTotal());
        }

        return total;
    }

}
