package pl.softwaremind.ckjava.recommendation.groovy.training;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.*;
import static pl.softwaremind.ckjava.recommendation.groovy.training.OrderItemBuilder.orderItem;

public class OrderTest {

    @Test(expectedExceptions = OrderException.class)
    public void shallNotAllowToCloseEmptyOrder() {
        // given
        final Order order = new Order("order number 1234");

        // when
        order.close();

        // then
        // exception expected
    }

    @Test(expectedExceptions = OrderException.class)
    public void shallNotAllowToAddItemsToAlreadyClosedOrder() {
        // given
        final Order order = new Order("order number 1234")
                .addItem(orderItem().build())
                .close();

        // when
        order.addItem(orderItem().build());

        // then
        // exception expected
    }

    @Test
    public void shallCLoseOnAlreadyClosedOrderHaveNoEffect() {
        // given
        final Order order = new Order("order number 1234")
                .addItem(orderItem().build())
                .close();

        // when
        order.close();

        // then
        assertTrue(order.isClosed(), "Order shall be closed but is not!");
    }

    @Test(expectedExceptions = OrderException.class)
    public void shallNotAllowToAddItemWithTheSameCodeTwice() {
        // given
        final OrderItem item = orderItem().build();
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        order.addItem(orderItem()
                .withCode(item.getCode())
                .build());

        // then
        // exception expected
    }

    @Test
    public void shallFindAddedItemByItsCode() {
        // given
        final OrderItem item = orderItem().build();
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        final OrderItem itemByCode = order.getItemByCode(item.getCode());

        // then
        assertSame(itemByCode, item, "OrderItem returned by getItemByCode() shall return the same instance as was added!");
    }

    @Test
    public void shallReturnNullIfItemIsNotFoundByCode() {
        // given
        final OrderItem item = orderItem().build();
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        final OrderItem itemByCode = order.getItemByCode("other item code than " + item.getCode());

        // then
        assertNull(itemByCode, "OrderItem returned by getItemByCode() shall be null!");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void shallNotAllowToAddItemsToCollectionObtainedByGetItems() {
        // given
        final Order order = new Order("order number 1234");

        // when
        order.getItems().add(orderItem().build());

        // then
        // exception expected
    }

    @Test
    public void shallReturnAllTheItemsAddedWhenGetItemsUsedInTheSameOrderAsAdded() {
        // given
        final Order order = new Order("order number 1234");

        final int itemCount = ThreadLocalRandom.current().nextInt(1, 15);

        final List<OrderItem> itemsAdded = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build();
            order.addItem(item);
            itemsAdded.add(item);
        }

        // when
        final Collection<OrderItem> items = order.getItems();

        // then
        assertEquals(items, itemsAdded, "Items returned by Order.getItems() do not match the items added!");
    }

    @Test
    public void shallReturnCorrectNetTotal() {
        // given
        final Order order = new Order("order number 1234");

        final int itemCount = ThreadLocalRandom.current().nextInt(1, 15);

        BigDecimal expectedNetTotal = BigDecimal.ZERO;
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build();
            order.addItem(item);

            expectedNetTotal = expectedNetTotal.add(item.getNetTotal());
        }

        // when
        final BigDecimal netTotal = order.getNetTotal();

        // then
        assertEquals(netTotal, expectedNetTotal);
    }

    @Test
    public void shallReturnCorrectGrossTotal() {
        // given
        final Order order = new Order("order number 1234");

        final int itemCount = ThreadLocalRandom.current().nextInt(1, 15);

        BigDecimal expectedGrossTotal = BigDecimal.ZERO;
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build();
            order.addItem(item);

            expectedGrossTotal = expectedGrossTotal.add(item.getGrossTotal());
        }

        // when
        final BigDecimal grossTotal = order.getGrossTotal();

        // then
        assertEquals(grossTotal, expectedGrossTotal);
    }

    private String randomCode() {
        return randomAlphanumeric(10);
    }

}
