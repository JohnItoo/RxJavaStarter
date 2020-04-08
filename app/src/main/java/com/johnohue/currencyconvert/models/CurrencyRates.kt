package com.johnohue.currencyconvert.models

import java.math.BigDecimal
import java.util.Currency

data class CurrencyRates(
    val baseCurrency: Currency,
    val currentRates: Map<Currency, BigDecimal>
)

data class CurrencyRateItem(
    val name: String,
    val value: Double,
    val editable: Boolean = false
)

