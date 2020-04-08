package com.johnohue.currencyconvert.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.johnohue.currencyconvert.core.repository.CurrencyRepository
import com.johnohue.currencyconvert.foundation.BaseViewModel
import com.johnohue.currencyconvert.models.BaseCurrency
import com.johnohue.currencyconvert.models.CurrencyRateItem
import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import com.johnohue.currencyconvert.utils.CurrencyRateValueConverter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CurrencyListViewModel @Inject constructor(
    private val ratesRepository: CurrencyRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : BaseViewModel() {

    private val _currencyExchangeLiveDataStub = MutableLiveData<List<CurrencyRateItem>>(emptyList())

    val currencies: LiveData<List<CurrencyRateItem>>
        get() = _currencyExchangeLiveDataStub

    private val baseCurrencyObservable: BehaviorSubject<BaseCurrency> =
        BehaviorSubject.createDefault(ApplicationConfigurationUtils().defaultCurrency)

    fun subscribeToRates() {
        Observable.combineLatest(
            baseCurrencyObservable.subscribeOn(schedulerProvider.io()),
            ratesObservable().subscribeOn(schedulerProvider.io()),
            BiFunction<BaseCurrency, CurrencyRatesResponse, List<CurrencyRateItem>> { baseCurrency: BaseCurrency, ratesResponse: CurrencyRatesResponse ->
                mergeBaseSelectionAndResponse(baseCurrency, ratesResponse)
            })
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { items ->
                    _hasError.value = false
                    _currencyExchangeLiveDataStub.value = items
                },
                { error ->
                    if (_currencyExchangeLiveDataStub.value.isNullOrEmpty()) {
                        _hasError.value = true
                    }
                }
            ).addTo(disposable)
    }

    private fun ratesObservable(): Observable<CurrencyRatesResponse> =
        baseCurrencyObservable.switchMap { baseCurrency ->
            ratesRepository.getCurrencyRates(
                baseCurrency.currencyCode
            )
        }

    private fun mergeBaseSelectionAndResponse(
        baseCurrency: BaseCurrency,
        ratesResponse: CurrencyRatesResponse
    ): List<CurrencyRateItem> {
        val currencyRateItems =
            ratesResponse.currentRates.entries.map { entry: Map.Entry<String, Double> ->
                CurrencyRateItem(
                    entry.key,
                    entry.value * baseCurrency.amount
                )
            }
        return mutableListOf(baseCurrency.toBaseCurrencyRateItem()).apply {
            addAll(currencyRateItems)
        }
    }

    fun onItemClicked(item: CurrencyRateItem) {
        val currentBase = baseCurrencyObservable.value!!
        val newBase = currentBase.copy(amount = item.value, currencyCode = item.name)
        if (newBase.currencyCode != currentBase.currencyCode) {
            Log.d("CurrencyListViewModel", newBase.currencyCode)
            baseCurrencyObservable.onNext(newBase)
        }
    }

    fun onCurrencyAmountChanged(newValue: String) {
        val currentBase = baseCurrencyObservable.value!!
        val newBase =
            currentBase.copy(
                amount = CurrencyRateValueConverter.stringToDouble(newValue),
                currencyCode = currentBase.currencyCode
            )
        if (newBase != currentBase) {
            Log.d("CurrencyListViewModel", newBase.currencyCode)
            baseCurrencyObservable.onNext(newBase)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onPause() {
        disposable.clear()
    }

    fun onResume() {
        subscribeToRates()
    }
}