package pl.softwaremind.ckjava.recommendation.groovy.training;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class OrderItemTest {

    @DataProvider
    public Object[][] netTotals() {
        return new Object[][] {
            { new BigDecimal("2.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("6.00") },
            { new BigDecimal("3.00"), new BigDecimal("2.00"), new BigDecimal("0.23"), new BigDecimal("6.00") },
            { new BigDecimal("0.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("0.00") },
            { new BigDecimal("2.10"), new BigDecimal("3.50"), new BigDecimal("0.00"), new BigDecimal("7.35") },
            { new BigDecimal("0.10"), new BigDecimal("0.05"), new BigDecimal("0.00"), new BigDecimal("0.01") },
            { new BigDecimal("0.10"), new BigDecimal("0.04"), new BigDecimal("0.00"), new BigDecimal("0.00") },
        };
    }

    @Test(dataProvider = "netTotals")
    public void shallCalculateNetTotalCorrectly(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedNetTotal) {
        // given
        final OrderItem item = new OrderItem("code", "name", quantity, netPricePerPiece, vatRate);

        // when
        final BigDecimal actualNetTotal = item.getNetTotal();

        // then
        assertEquals(actualNetTotal, expectedNetTotal, String.format("Expected net total of %s but got %s", expectedNetTotal, actualNetTotal));
    }

    @DataProvider
    public Object[][] grossTotals() {
        return new Object[][] {
                { new BigDecimal("2.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("7.38") },
                { new BigDecimal("3.00"), new BigDecimal("2.00"), new BigDecimal("0.23"), new BigDecimal("7.38") },
                { new BigDecimal("0.00"), new BigDecimal("3.00"), new BigDecimal("0.23"), new BigDecimal("0.00") },
                { new BigDecimal("2.10"), new BigDecimal("3.50"), new BigDecimal("0.07"), new BigDecimal("7.86") },
                { new BigDecimal("0.10"), new BigDecimal("0.05"), new BigDecimal("0.07"), new BigDecimal("0.01") },
                { new BigDecimal("0.10"), new BigDecimal("0.04"), new BigDecimal("0.07"), new BigDecimal("0.00") },
        };
    }

    @Test(dataProvider = "grossTotals")
    public void shallCalculateGrossTotalCorrectly(BigDecimal quantity, BigDecimal netPricePerPiece, BigDecimal vatRate, BigDecimal expectedGrossTotal) {
        // given
        final OrderItem item = new OrderItem("code", "name", quantity, netPricePerPiece, vatRate);

        // when
        final BigDecimal actualGrossTotal = item.getGrossTotal();

        // then
        assertEquals(actualGrossTotal, expectedGrossTotal, String.format("Expected gross total of %s but got %s", expectedGrossTotal, actualGrossTotal));
    }

}
