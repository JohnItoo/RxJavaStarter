package com.johnohue.currencyconvert.models

data class BaseCurrency(val amount: Double, val currencyCode: String) {

    fun toBaseCurrencyRateItem() =
        CurrencyRateItem(currencyCode, amount, editable = true)
}