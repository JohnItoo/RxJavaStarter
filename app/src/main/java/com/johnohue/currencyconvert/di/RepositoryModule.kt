package com.johnohue.currencyconvert.di

import com.johnohue.currencyconvert.core.network.CurrencyService
import com.johnohue.currencyconvert.core.repository.CurrencyRepository
import com.johnohue.currencyconvert.core.repository.CurrencyRepositoryImpl
import com.johnohue.currencyconvert.list.BaseSchedulerProvider
import com.johnohue.currencyconvert.list.SchedulerProvider
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesCurrencyExchangeRatesRepository(
                                                configUtils: ApplicationConfigurationUtils,
                                                currencyService: CurrencyService): CurrencyRepository =
       CurrencyRepositoryImpl( configUtils, currencyService)

    @Singleton
    @Provides
    fun providesBaseScheduler() : BaseSchedulerProvider = SchedulerProvider()
}