package pl.softwaremind.ckjava.recommendation.groovy.training.domain

import spock.lang.Specification
import spock.lang.Unroll

class OrderTest extends Specification {

    def 'shall not allow to close empty order'() {
        given: 'order with no items'
        def order = new Order('order number 1234')

        when: 'order is being closed'
        order.close()

        then: 'close action is not allowed'
        thrown(OrderException)
    }

    def 'shall not allow to add items to already closed order'() {
        given: 'closed order with some items'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()

        when: 'new item is being added'
        order.addItem(OrderItem.withDefault())

        then: 'addition is not allowed'
        thrown(OrderException)
    }

    def 'shall close on already closed order have no effect'() {
        given: 'closed order with some items'
        def order = new Order('order number 1234')
                .addItem(OrderItem.withDefault())
                .close()

        when: 'order is being closed again'
        order.close()

        then: 'order shall still be closed'
        order.closed
    }

    def 'shall not allow to add item with the same code twice'() {
        given: 'order with some order item'
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        when: 'order item with the same code as before is being added'
        order.addItem(OrderItem.with(code: item.code))

        then: 'addition is not allowed'
        thrown(OrderException)
    }

    def "shall find added item by it's code"() {
        given: 'order with some item'
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        expect: "added item can be found within order by it's code"
        order.getItemByCode(item.code)?.is(item)
    }

    def 'shall return null if item is not found by code'() {
        given: 'order with some items'
        def item = OrderItem.withDefault()
        def order = new Order('order number 1234')
                .addItem(item)

        expect: 'order item which is not a part of order shall not be found by code'
        order.getItemByCode("other item code than $item.code") == null
    }

    @Unroll
    def 'shall not allow to add items to collection obtained by getItems() from #orderDesc'() {
        given: 'any order'
        when: 'order item is being added to order via list of items obtained by Order.getItems()'
        order.items.add(OrderItem.withDefault())

        then: 'addition is not allowed'
        thrown(UnsupportedOperationException)

        where:
        orderDesc                    || order
        'empty order'                || new Order('order number 1234')
        'open order with some items' || new Order('order number 1234').addItem(OrderItem.withDefault())
        'closed order'               || new Order('order number 1234').addItem(OrderItem.withDefault()).close()
    }

    def 'shall return all the items added when getItems() called in the same order as added'() {
        given: 'order'
        def order = new Order('order number 1234')
        and: 'some number of items is added to the order'
        def itemsAdded = []
        Integer.randomBetween(1, 15).times {
            def item = OrderItem.withDefault()
            order.addItem(item)
            itemsAdded << item
        }

        expect: 'all the added items shall be returned by Order.getItems()'
        order.items as List == itemsAdded
    }

    def 'shall calculate totals correctly'() {
        given: 'order'
        def order = new Order('order number 1234')
        and: 'some number of items are added to the order'
        List<OrderItem> itemsAdded = []
        Integer.randomBetween(1, 15).times {
            def item = OrderItem.withDefault()
            order.addItem(item)
            itemsAdded << item
        }

        expect: "order's net total shall be sum of all items' net total"
        order.netTotal   == itemsAdded*.netTotal.sum()
        and: "order's gross total shall be sum of all items' gross total"
        order.grossTotal == itemsAdded*.grossTotal.sum()
    }

}
