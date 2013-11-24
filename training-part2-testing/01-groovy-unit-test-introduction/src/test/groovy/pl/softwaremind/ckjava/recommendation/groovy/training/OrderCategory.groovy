package pl.softwaremind.ckjava.recommendation.groovy.training

@Category(Order)
class OrderCategory {

    Order addItem(OrderItemBuilder builder) {
        this.addItem(builder.build())
    }

}
