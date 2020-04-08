package com.johnohue.currencyconvert.core.network

import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Currency

interface CurrencyService {
    @GET("latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Single<CurrencyRatesResponse>
}