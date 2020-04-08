package com.johnohue.currencyconvert.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyRateValueConverterTest {

    @Test
    fun `get Double Value of a Parseable String Pass`() =
       assert(CurrencyRateValueConverter.stringToDouble("1.2").equals(1.2))


    @Test
    fun `get Double Value of an Unparseable String Fail`() =
        assert(CurrencyRateValueConverter.stringToDouble("absolutely trash").equals(0.0))

    @Test
    fun `get String Value from a Double Pass`() =
        assert(CurrencyRateValueConverter.doubleToString(1.36).equals("1.36"))
}