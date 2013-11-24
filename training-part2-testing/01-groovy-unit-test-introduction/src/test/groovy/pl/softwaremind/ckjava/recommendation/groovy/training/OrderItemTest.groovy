package pl.softwaremind.ckjava.recommendation.groovy.training
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class OrderItemTest {

    @DataProvider
    Object[][] 'invalid params'() {
        [
                // invalid code
                [ null, 'name', 1.00, 2.00, 0.23 ],
                [ '', 'name', 1.00, 2.00, 0.23 ],

                // invalid name
                [ 'code', null, 1.00, 2.00, 0.23 ],
                [ 'code', '', 1.00, 2.00, 0.23 ],

                // invalid quantity
                [ 'code', 'name', null, 2.00, 0.23 ],

                // invalid price
                [ 'code', 'name', 1.00, null, 0.23 ],
                [ 'code', 'name', 1.00, -2.00, 0.23 ],

                // invalid vat rate
                [ 'code', 'name', 1.00, 2.00, null ],
                [ 'code', 'name', 1.00, 2.00, -0.10 ],
                [ 'code', 'name', 1.00, 2.00, 1.01 ],
                [ 'code', 'name', 1.00, 2.00, 23.00 ],
        ]
    }

    @Test(dataProvider = 'invalid params', expectedExceptions = OrderException)
    void 'shall not allow to create OrderItem with invalid parameters'(String code, String name, BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate) {
        // when
        new OrderItem(code, name, quantity, netPricePerPiece, vatRate)

        // then
        // exception expected
    }

    @DataProvider
    Object[][] 'net totals'() {
        [
            [ 2.00, 3.00, 0.23, 6.00 ],
            [ 3.00, 2.00, 0.23, 6.00 ],
            [ 0.00, 3.00, 0.23, 0.00 ],
            [ 2.10, 3.50, 0.00, 7.35 ],
            [ 0.10, 0.05, 0.00, 0.01 ],
            [ 0.10, 0.04, 0.00, 0.00 ],
        ]
    }

    @Test(dataProvider = 'net totals')
    void 'shall calculate net total correctly'(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedNetTotal) {
        // given
        def item = new OrderItem('code', 'name', quantity, netPricePerPiece, vatRate)

        // when
        // then
        assert item.getNetTotal() == expectedNetTotal
    }

    @DataProvider
    Object[][] 'gross totals'() {
        [
                [ 2.00, 3.00, 0.23, 7.38 ],
                [ 3.00, 2.00, 0.23, 7.38 ],
                [ 0.00, 3.00, 0.23, 0.00 ],
                [ 2.10, 3.50, 0.07, 7.86 ],
                [ 0.10, 0.05, 0.07, 0.01 ],
                [ 0.10, 0.04, 0.07, 0.00 ],
        ]
    }

    @Test(dataProvider = 'gross totals')
    void 'shall calculate gross total correctly'(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedGrossTotal) {
        // given
        def item = new OrderItem('code', 'name', quantity, netPricePerPiece, vatRate)

        // when
        // then
        assert item.getGrossTotal() == expectedGrossTotal
    }

}
