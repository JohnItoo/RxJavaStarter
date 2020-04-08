package com.johnohue.currencyconvert.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.johnohue.currencyconvert.list.CurrencyListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyListViewModel::class)
    abstract fun bindCurrencyViewModel(viewModel: CurrencyListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}