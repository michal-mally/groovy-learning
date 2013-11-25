package pl.softwaremind.ckjava.recommendation.groovy.training

import spock.lang.Specification
import spock.lang.Unroll

class OrderItemTest extends Specification {

    @Unroll
    def 'shall not allow to create OrderItem with invalid parameters (#code, #name, #quantity, #netPricePerPiece, #vatRate)'() {
        when: 'order item is being created with invalid parameters'
        new OrderItem(code, name, quantity, netPricePerPiece, vatRate)

        then: 'order item shall not be created successfully'
        thrown(OrderException)

        where:
        code   | name   | quantity | netPricePerPiece | vatRate
        null   | 'name' | 1.00     | 2.00             | 0.23
        ''     | 'name' | 1.00     | 2.00             | 0.23
        'code' | null   | 1.00     | 2.00             | 0.23
        'code' | ''     | 1.00     | 2.00             | 0.23
        'code' | 'name' | null     | 2.00             | 0.23
        'code' | 'name' | 1.00     | null             | 0.23
        'code' | 'name' | 1.00     | -2.00            | 0.23
        'code' | 'name' | 1.00     | 2.00             | null
        'code' | 'name' | 1.00     | 2.00             | -0.10
        'code' | 'name' | 1.00     | 2.00             | 1.01
        'code' | 'name' | 1.00     | 2.00             | 23.00
    }

    @Unroll
    def 'shall net total = #netTotal and gross total = #grossTotal when quantity = #quantity, net price per piece = #netPricePerPiece, vatRate = #vatRate'() {
        given: 'order item'
        def item = new OrderItem('code', 'name', quantity, netPricePerPiece, vatRate)

        expect: 'net total = quantity x net price per piece'
        item.netTotal   == netTotal
        and: 'gross total = net total x (1 + vat rate)'
        item.grossTotal == grossTotal

        where:
        quantity | netPricePerPiece | vatRate || netTotal | grossTotal
        2.00     | 3.00             | 0.23    || 6.00     | 7.38
        3.00     | 2.00             | 0.23    || 6.00     | 7.38
        0.00     | 3.00             | 0.23    || 0.00     | 0.00
        2.10     | 3.50             | 0.07    || 7.35     | 7.86
        0.10     | 0.05             | 0.07    || 0.01     | 0.01
        0.10     | 0.04             | 0.07    || 0.00     | 0.00
    }

}
