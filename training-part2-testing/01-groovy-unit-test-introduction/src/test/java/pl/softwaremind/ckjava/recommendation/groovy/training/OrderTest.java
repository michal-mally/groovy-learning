package pl.softwaremind.ckjava.recommendation.groovy.training;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

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
                .addItem(orderItem("item 1"))
                .close();

        // when
        order.addItem(orderItem("item 2"));

        // then
        // exception expected
    }

    @Test
    public void shallCLoseOnAlreadyClosedOrderHaveNoEffect() {
        // given
        final Order order = new Order("order number 1234")
                .addItem(orderItem("item 1"))
                .close();

        // when
        order.close();

        // then
        assertTrue(order.isClosed(), "Order shall be closed but is not!");
    }

    @Test(expectedExceptions = OrderException.class)
    public void shallNotAllowToAddItemWithTheSameCodeTwice() {
        // given
        final String orderItemCode = "item 1";
        final Order order = new Order("order number 1234")
                .addItem(orderItem(orderItemCode));

        // when
        order.addItem(orderItem(orderItemCode));

        // then
        // exception expected
    }

    @Test
    public void shallFindAddedItemByItsCode() {
        // given
        final String orderItemCode = "item 1";
        final OrderItem item = orderItem(orderItemCode);
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        final OrderItem itemByCode = order.getItemByCode(orderItemCode);

        // then
        assertSame(itemByCode, item, "OrderItem returned by getItemByCode() shall return the same instance as was added!");
    }

    @Test
    public void shallReturnNullIfItemIsNotFoundByCode() {
        // given
        final String orderItemCode = "item 1";
        final OrderItem item = orderItem(orderItemCode);
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        final OrderItem itemByCode = order.getItemByCode("item 2");

        // then
        assertNull(itemByCode, "OrderItem returned by getItemByCode() shall be null!");
    }

    private OrderItem orderItem(String code) {
        return new OrderItem(code, "name", new BigDecimal("1.00"), new BigDecimal("2.43"), new BigDecimal("0.23"));
    }

}
