package com.johnohue.currencyconvert.core.repository

import android.util.Log
import com.johnohue.currencyconvert.core.network.CurrencyService
import com.johnohue.currencyconvert.models.BaseCurrency
import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class CurrencyRepositoryImpl(
  private val configUtils: ApplicationConfigurationUtils,
    private val currencyService: CurrencyService
) :
    CurrencyRepository {

    override fun getCurrencyRates(base: String): Observable<CurrencyRatesResponse> =
        Observable.interval(configUtils.initialDelay,  configUtils.repeatingConstant, configUtils.timeUnit)
            .flatMapSingle { currencyService.getCurrencyRates(base) }

    override fun getDefaultBaseCurrency(): BaseCurrency = configUtils.defaultCurrency
}