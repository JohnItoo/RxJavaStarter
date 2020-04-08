package com.johnohue.currencyconvert.core.repository

import com.johnohue.currencyconvert.models.BaseCurrency
import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import io.reactivex.Observable


interface CurrencyRepository {
    fun getCurrencyRates(base: String): Observable<CurrencyRatesResponse>

    fun getDefaultBaseCurrency() :BaseCurrency

}