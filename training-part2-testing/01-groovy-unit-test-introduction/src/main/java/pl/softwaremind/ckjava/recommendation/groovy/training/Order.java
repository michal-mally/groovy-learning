package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Order {

    private final String number;

    private final List<OrderItem> items = new ArrayList<>();

    private boolean closed = false;

    public Order(String number) {
        this.number = number;
    }

    public Order addItem(OrderItem item) {
        if (isClosed()) {
            throw new OrderException("Cannot add items to already closed Order!");
        }

        if (getItemByCode(item.getCode()) != null) {
            throw new OrderException("Cannot add two items with the same code to the same Order!");
        }

        this.items.add(item);
        return this;
    }

    public List<OrderItem> getItems() {
        return unmodifiableList(this.items);
    }

    public OrderItem getItemByCode(String code) {
        for (OrderItem item : this.items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }

        return null;
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
        for (OrderItem item : this.items) {
            total = total.add(item.getNetTotal());
        }

        return total;
    }

    public BigDecimal getGrossTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : this.items) {
            total = total.add(item.getGrossTotal());
        }

        return total;
    }

}
