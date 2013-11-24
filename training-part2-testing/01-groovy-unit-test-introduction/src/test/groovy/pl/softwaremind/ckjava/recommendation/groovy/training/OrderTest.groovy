package pl.softwaremind.ckjava.recommendation.groovy.training
import org.testng.annotations.Test

import java.util.concurrent.ThreadLocalRandom

import static pl.softwaremind.ckjava.recommendation.groovy.training.OrderItemBuilder.orderItem

class OrderTest {

    @Test(expectedExceptions = OrderException)
    void 'shall not allow to close empty order'() {
        // given
        def order = new Order('order number 1234')

        // when
        order.close()

        // then
        // exception expected
    }

    @Test(expectedExceptions = OrderException)
    void 'shall not allow to add items to already closed order'() {
        // given
        def order = new Order('order number 1234')
                .addItem(orderItem().build())
                .close()

        // when
        order.addItem(orderItem().build())

        // then
        // exception expected
    }

    @Test
    void 'shall cLose on already closed order have no effect'() {
        // given
        def order = new Order('order number 1234')
                .addItem(orderItem().build())
                .close()

        // when
        order.close()

        // then
        assert order.isClosed()
    }

    @Test(expectedExceptions = OrderException)
    void 'shall not allow to add item with the same code twice'() {
        // given
        def item = orderItem().build()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        order.addItem(orderItem()
                .withCode(item.getCode())
                .build())

        // then
        // exception expected
    }

    @Test
    void 'shall find added item by it\'s code'() {
        // given
        def item = orderItem().build()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        def itemByCode = order.getItemByCode(item.getCode())

        // then
        assert itemByCode != null && itemByCode.is(item)
    }

    @Test
    void 'shall return null if item is not found by code'() {
        // given
        def item = orderItem().build()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        def itemByCode = order.getItemByCode('other item code than ' + item.getCode())

        // then
        assert itemByCode == null
    }

    @Test(expectedExceptions = UnsupportedOperationException)
    void 'shall not allow to add items to collection obtained by getItems()'() {
        // given
        def order = new Order('order number 1234')

        // when
        order.getItems().add(orderItem().build())

        // then
        // exception expected
    }

    @Test
    void 'shall return all the items added when getItems() called in the same order as added'() {
        // given
        def order = new Order('order number 1234')

        def itemCount = ThreadLocalRandom.current().nextInt(1, 15)

        def itemsAdded = new ArrayList<>()
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build()
            order.addItem(item)
            itemsAdded.add(item)
        }

        // when
        def getItems = order.getItems() as List<OrderItem>

        // then
        assert getItems == itemsAdded
    }

    @Test
    void 'shall return correct net total'() {
        // given
        def order = new Order('order number 1234')

        def itemCount = ThreadLocalRandom.current().nextInt(1, 15)

        def expectedNetTotal = 0.00
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build()
            order.addItem(item)

            expectedNetTotal = expectedNetTotal.add(item.getNetTotal())
        }

        // when
        def netTotal = order.getNetTotal()

        // then
        assert netTotal == expectedNetTotal
    }

    @Test
    void 'shall return correct gross total'() {
        // given
        def order = new Order('order number 1234')

        def itemCount = ThreadLocalRandom.current().nextInt(1, 15)

        def expectedGrossTotal = 0.00
        for (int i = 0; i < itemCount; i++) {
            final OrderItem item = orderItem().build()
            order.addItem(item)

            expectedGrossTotal = expectedGrossTotal.add(item.getGrossTotal())
        }

        // when
        def grossTotal = order.getGrossTotal()

        // then
        assert grossTotal == expectedGrossTotal
    }

}
