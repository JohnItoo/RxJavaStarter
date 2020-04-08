package com.johnohue.currencyconvert.utils

import com.johnohue.currencyconvert.BuildConfig
import com.johnohue.currencyconvert.models.BaseCurrency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.concurrent.TimeUnit

data class ApplicationConfigurationUtils(
    val apiEndpoint: String = BuildConfig.API_ENDPOINT,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    val repeatingConstant: Long = 1,
    val defaultCurrency: BaseCurrency = BaseCurrency(10.0, "EUR"),
    val initialDelay: Long = 0
)


object CurrencyRateValueConverter {

    @JvmStatic
    fun doubleToString(value: Double): String = getDecimalFormat().format(value)

    fun stringToDouble(value: String): Double {
        return try {
            getDecimalFormat().parse(value)?.toDouble() ?: 0.0
        } catch (ex: ParseException) {
            0.0
        }
    }

    private fun getSymbols(): DecimalFormatSymbols =
        DecimalFormatSymbols().apply {
            decimalSeparator = '.'
        }

    private fun getDecimalFormat(): DecimalFormat {

        return DecimalFormat().apply {
            setDecimalFormatSymbols(getSymbols())
            minimumFractionDigits = 0
            maximumFractionDigits = 2
            isGroupingUsed = false
        }
    }
}