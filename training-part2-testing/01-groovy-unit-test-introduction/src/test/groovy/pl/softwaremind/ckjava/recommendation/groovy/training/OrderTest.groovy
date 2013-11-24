package pl.softwaremind.ckjava.recommendation.groovy.training

import org.testng.annotations.Test

import static java.util.concurrent.ThreadLocalRandom.current as random
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
                .addItem(orderItem())
                .close()

        // when
        order.addItem(orderItem())

        // then
        // exception expected
    }

    @Test
    void 'shall cLose on already closed order have no effect'() {
        // given
        def order = new Order('order number 1234')
                .addItem(orderItem())
                .close()

        // when
        order.close()

        // then
        assert order.isClosed()
    }

    @Test(expectedExceptions = OrderException)
    void 'shall not allow to add item with the same code twice'() {
        // given
        def item = orderItem()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        order.addItem(orderItem(code: item.code))

        // then
        // exception expected
    }

    @Test
    void 'shall find added item by it\'s code'() {
        // given
        def item = orderItem()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        // then
        assert order.getItemByCode(item.code)?.is(item)
    }

    @Test
    void 'shall return null if item is not found by code'() {
        // given
        def item = orderItem()
        def order = new Order('order number 1234')
                .addItem(item)

        // when
        // then
        assert order.getItemByCode("other item code than $item.code") == null
    }

    @Test(expectedExceptions = UnsupportedOperationException)
    void 'shall not allow to add items to collection obtained by getItems()'() {
        // given
        def order = new Order('order number 1234')

        // when
        order.items.add(orderItem())

        // then
        // exception expected
    }

    @Test
    void 'shall return all the items added when getItems() called in the same order as added'() {
        // given
        def order = new Order('order number 1234')

        def itemsAdded = []
        random().nextInt(1, 15).times {
            def item = orderItem()
            order.addItem(item)
            itemsAdded << item
        }
        // when
        // then
        assert order.items as List == itemsAdded
    }

    @Test
    void 'shall return correct net total'() {
        // given
        def order = new Order('order number 1234')

        List<OrderItem> itemsAdded = []
        random().nextInt(1, 15).times {
            def item = orderItem()
            order.addItem(item)
            itemsAdded << item
        }

        // when
        // then
        assert order.netTotal == itemsAdded*.netTotal.sum()
    }

    @Test
    void 'shall return correct gross total'() {
        // given
        def order = new Order('order number 1234')

        List<OrderItem> itemsAdded = []
        random().nextInt(1, 15).times {
            def item = orderItem()
            order.addItem(item)
            itemsAdded << item
        }

        // when
        // then
        assert order.grossTotal == itemsAdded*.grossTotal.sum()
    }

}
