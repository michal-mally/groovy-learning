package pl.softwaremind.ckjava.recommendation.groovy.training.extension

import static java.util.concurrent.ThreadLocalRandom.current as random

class BigDecimalStaticExtension {

    static BigDecimal randomBetween(BigDecimal selfType, BigDecimal from, BigDecimal to) {
        assert from < to
        assert from.scale() == to.scale()

        def fromLong = from.unscaledValue() as Long
        def toLong = to.unscaledValue() as Long
        def betweenLong = random().nextLong(fromLong, toLong)
        new BigDecimal(betweenLong as BigInteger, from.scale())
    }

}
