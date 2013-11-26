package pl.softwaremind.ckjava.recommendation.groovy.training

import spock.lang.Specification
import spock.lang.Unroll

class OrderItemTest extends Specification {

    def params = [code: 'code', name: 'name', quantity: 2.00, netPricePerPiece: 3.00, vatRate: 0.23]

    @Unroll
    def 'shall not allow to create OrderItem with #paramName = #paramValue'() {
        given: 'one of the parameter has invalid value'
        params[paramName] = paramValue

        when: 'order item is being created'
        new OrderItem(params.code, params.name, params.quantity, params.netPricePerPiece, params.vatRate)

        then: 'order item shall not be created successfully'
        thrown(OrderException)

        where:
        paramName          | paramValue
        'code'             | null
        'code'             | ''
        'name'             | null
        'name'             | ''
        'quantity'         | null
        'netPricePerPiece' | null
        'netPricePerPiece' | -2.00
        'vatRate'          | null
        'vatRate'          | -0.10
        'vatRate'          | 1.01
        'vatRate'          | 23.00
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
