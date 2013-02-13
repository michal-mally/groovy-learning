package pl.helenium.learning.groovy.gpars.mapreduce

import static groovyx.gpars.GParsPool.withPool
import static java.util.concurrent.ThreadLocalRandom.current as random
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static pl.helenium.learning.groovy.gpars.Time.time

Collection.metaClass.random << {
    delegate[random().nextInt(delegate.size())]
}

Integer.metaClass.random << {
    random().nextInt(delegate) + 1
}

class OrderItem {

    int units

    BigDecimal unitPrice

}

class Order {

    String customer

    List<OrderItem> items = []

}

def customers = (1..20).collect { randomAlphanumeric(5) }

def orders = (1..10_000).collect {
    new Order(
            customer: customers.random(),
            items: (1..10.random()).collect {
                new OrderItem(
                        units: 10.random(),
                        unitPrice: BigDecimal.valueOf(random().nextInt(100_00), 2)
                )
            }
    )
}

time id: 'orderProcessing', {
    withPool {
        orders.parallel
                .map { order -> order.items.collectMany { [order.customer, it.units * it.unitPrice] } }
                .combine(0.00) { sum, val -> sum + val}.getParallel()
                .sort { -it.value }
                .collection
                .each { println "${it.key} -> ${it.value}" }
    }
}

