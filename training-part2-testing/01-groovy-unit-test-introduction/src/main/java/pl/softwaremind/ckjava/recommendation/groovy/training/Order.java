package pl.softwaremind.ckjava.recommendation.groovy.training;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Order {

    private final List<OrderItem> items = new ArrayList<>();

    private boolean closed = false;

    public Order addItem(OrderItem item) {
        if (isClosed()) {
            throw new OrderAlreadyClosedException("Cannot add items to already closed Order!");
        }

        this.items.add(item);
        return this;
    }

    public List<OrderItem> getItems() {
        return unmodifiableList(this.items);
    }

    public Order close() {
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
