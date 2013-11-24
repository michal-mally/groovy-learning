package pl.softwaremind.ckjava.recommendation.groovy.training
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class OrderItemTest {

    @DataProvider
    Object[][] invalidParams() {
        [
                // invalid code
                [ null, "name", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("0.23") ],
                [ "", "name", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("0.23") ],

                // invalid name
                [ "code", null, new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("0.23") ],
                [ "code", "", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("0.23") ],

                // invalid quantity
                [ "code", "name", null, new BigDecimal("2.00"), new BigDecimal("0.23") ],

                // invalid price
                [ "code", "name", new BigDecimal("1.00"), null, new BigDecimal("0.23") ],
                [ "code", "name", new BigDecimal("1.00"), new BigDecimal("-2.00"), new BigDecimal("0.23") ],

                // invalid vat rate
                [ "code", "name", new BigDecimal("1.00"), new BigDecimal("2.00"), null ],
                [ "code", "name", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("-0.10") ],
                [ "code", "name", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("1.01") ],
                [ "code", "name", new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("23.00") ],
        ]
    }

    @Test(dataProvider = "invalidParams", expectedExceptions = OrderException.class)
    void shallNotAllowToCreateOrderItemWithInvalidParameters(String code, String name, BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate) {
        // when
        new OrderItem(code, name, quantity, netPricePerPiece, vatRate)

        // then
        // exception expected
    }

    @DataProvider
    Object[][] netTotals() {
        [
            [ new BigDecimal("2.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("6.00") ],
            [ new BigDecimal("3.00"), new BigDecimal("2.00"), new BigDecimal("0.23"), new BigDecimal("6.00") ],
            [ new BigDecimal("0.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("0.00") ],
            [ new BigDecimal("2.10"), new BigDecimal("3.50"), new BigDecimal("0.00"), new BigDecimal("7.35") ],
            [ new BigDecimal("0.10"), new BigDecimal("0.05"), new BigDecimal("0.00"), new BigDecimal("0.01") ],
            [ new BigDecimal("0.10"), new BigDecimal("0.04"), new BigDecimal("0.00"), new BigDecimal("0.00") ],
        ]
    }

    @Test(dataProvider = "netTotals")
    void shallCalculateNetTotalCorrectly(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedNetTotal) {
        // given
        final OrderItem item = new OrderItem("code", "name", quantity, netPricePerPiece, vatRate)

        // when
        final BigDecimal actualNetTotal = item.getNetTotal()

        // then
        assertEquals(actualNetTotal, expectedNetTotal, String.format("Expected net total of %s but got %s", expectedNetTotal, actualNetTotal))
    }

    @DataProvider
    Object[][] grossTotals() {
        [
                [ new BigDecimal("2.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("7.38") ],
                [ new BigDecimal("3.00"), new BigDecimal("2.00"), new BigDecimal("0.23"), new BigDecimal("7.38") ],
                [ new BigDecimal("0.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("0.00") ],
                [ new BigDecimal("2.10"), new BigDecimal("3.50"), new BigDecimal("0.07"), new BigDecimal("7.86") ],
                [ new BigDecimal("0.10"), new BigDecimal("0.05"), new BigDecimal("0.07"), new BigDecimal("0.01") ],
                [ new BigDecimal("0.10"), new BigDecimal("0.04"), new BigDecimal("0.07"), new BigDecimal("0.00") ],
        ]
    }

    @Test(dataProvider = "grossTotals")
    void shallCalculateGrossTotalCorrectly(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedGrossTotal) {
        // given
        final OrderItem item = new OrderItem("code", "name", quantity, netPricePerPiece, vatRate)

        // when
        final BigDecimal actualGrossTotal = item.getGrossTotal()

        // then
        assertEquals(actualGrossTotal, expectedGrossTotal, String.format("Expected gross total of %s but got %s", expectedGrossTotal, actualGrossTotal))
    }

}
