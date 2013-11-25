package pl.softwaremind.ckjava.recommendation.groovy.training

import spock.lang.Specification

class OrderTest extends Specification {

    def 'shall not allow to close empty order'() {
        given:
        def order = new Order('order number 1234')

        when:
        order.close()

        then:
        thrown(OrderException)
    }

    def 'shall not allow to add items to already closed order'() {
        given:
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()

        when:
        order.addItem(OrderItem.withDefault())

        then:
        thrown(OrderException)
    }

    def 'shall close on already closed order have no effect'() {
        given:
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()

        when:
        order.close()

        then:
        order.closed
    }

    def 'shall not allow to add item with the same code twice'() {
        given:
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        when:
        order.addItem(OrderItem.with(code: item.code))

        then:
        thrown(OrderException)
    }

    def 'shall find added item by it\'s code'() {
        given:
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        expect:
        order.getItemByCode(item.code)?.is(item)
    }

    def 'shall return null if item is not found by code'() {
        given:
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        expect:
        order.getItemByCode("other item code than $item.code") == null
    }

    def 'shall not allow to add items to collection obtained by getItems()'() {
        given:
        def order = new Order('order number 1234')

        when:
        order.items.add(OrderItem.withDefault())

        then:
        thrown(UnsupportedOperationException)
    }

    def 'shall return all the items added when getItems() called in the same order as added'() {
        given:
        def order = new Order('order number 1234')

        def itemsAdded = []
        Integer.randomBetween(1, 15).times {
            def item = OrderItem.withDefault()
            order.addItem(item)
            itemsAdded << item
        }

        expect:
        order.items as List == itemsAdded
    }

    def 'shall calculate totals correctly'() {
        given:
        def order = new Order('order number 1234')

        List<OrderItem> itemsAdded = []
        Integer.randomBetween(1, 15).times {
            def item = OrderItem.withDefault()
            order.addItem(item)
            itemsAdded << item
        }

        expect:
        order.netTotal   == itemsAdded*.netTotal.sum()
        order.grossTotal == itemsAdded*.grossTotal.sum()
    }

}
