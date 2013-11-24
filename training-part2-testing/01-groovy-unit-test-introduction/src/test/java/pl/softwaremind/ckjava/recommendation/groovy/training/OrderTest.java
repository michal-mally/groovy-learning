package pl.softwaremind.ckjava.recommendation.groovy.training;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.*;

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
                .addItem(orderItemWithCode("item 1"))
                .close();

        // when
        order.addItem(orderItemWithCode("item 2"));

        // then
        // exception expected
    }

    @Test
    public void shallCLoseOnAlreadyClosedOrderHaveNoEffect() {
        // given
        final Order order = new Order("order number 1234")
                .addItem(orderItemWithCode("item 1"))
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
                .addItem(orderItemWithCode(orderItemCode));

        // when
        order.addItem(orderItemWithCode(orderItemCode));

        // then
        // exception expected
    }

    @Test
    public void shallFindAddedItemByItsCode() {
        // given
        final String orderItemCode = "item 1";
        final OrderItem item = orderItemWithCode(orderItemCode);
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
        final OrderItem item = orderItemWithCode(orderItemCode);
        final Order order = new Order("order number 1234")
                .addItem(item);

        // when
        final OrderItem itemByCode = order.getItemByCode("item 2");

        // then
        assertNull(itemByCode, "OrderItem returned by getItemByCode() shall be null!");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void shallNotAllowToAddItemsToCollectionObtainedByGetItems() {
        // given
        final Order order = new Order("order number 1234");

        // when
        order.getItems().add(orderItemWithCode("item 1"));

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
            final OrderItem item = orderItemWithCode(randomCode());
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
            final BigDecimal quantity = randomBigDecimal(10_000);
            final BigDecimal netPricePerPiece = randomBigDecimal(10_000);
            final BigDecimal vatRate = randomBigDecimal(1_00);
            final OrderItem item = orderItemWithQuantityNetPricePerPieceAndVatRate(quantity, netPricePerPiece, vatRate);
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
            final BigDecimal quantity = randomBigDecimal(10_000);
            final BigDecimal netPricePerPiece = randomBigDecimal(10_000);
            final BigDecimal vatRate = randomBigDecimal(1_00);
            final OrderItem item = orderItemWithQuantityNetPricePerPieceAndVatRate(quantity, netPricePerPiece, vatRate);
            order.addItem(item);

            expectedGrossTotal = expectedGrossTotal.add(item.getGrossTotal());
        }

        // when
        final BigDecimal grossTotal = order.getGrossTotal();

        // then
        assertEquals(grossTotal, expectedGrossTotal);
    }

    private OrderItem orderItemWithCode(String code) {
        return new OrderItem(code, "name", new BigDecimal("1.00"), new BigDecimal("2.43"), new BigDecimal("0.23"));
    }


    private OrderItem orderItemWithQuantityNetPricePerPieceAndVatRate(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate) {
        return new OrderItem(randomCode(), "name", quantity, netPricePerPiece, vatRate);
    }

    private String randomCode() {
        return randomAlphanumeric(10);
    }

    private BigDecimal randomBigDecimal(long limit) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(limit), 2);
    }

}
