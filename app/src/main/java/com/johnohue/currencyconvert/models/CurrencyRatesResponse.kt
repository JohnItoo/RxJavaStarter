package com.johnohue.currencyconvert.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Currency

data class CurrencyRatesResponse(
    @SerializedName("baseCurrency")
    val baseCurrency: String,
    @SerializedName("rates")
    val currentRates: Map<String, Double>
)

fun CurrencyRatesResponse.asDomainModel(): CurrencyRates =
    CurrencyRates(
        Currency.getInstance(baseCurrency),
        currentRates
            .map { ratesMap ->
                Currency.getInstance(ratesMap.key) to BigDecimal(ratesMap.value)
            }.toMap()
    )