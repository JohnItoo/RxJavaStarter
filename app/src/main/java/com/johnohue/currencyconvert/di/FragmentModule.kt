package com.johnohue.currencyconvert.di

import androidx.fragment.app.Fragment
import com.johnohue.currencyconvert.list.CurrencyListFragment
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule : AndroidInjector<Fragment> {

    @ContributesAndroidInjector
    fun contributesCurrencyFragment(): CurrencyListFragment
}