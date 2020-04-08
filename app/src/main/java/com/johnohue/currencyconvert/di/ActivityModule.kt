package com.johnohue.currencyconvert.di

import com.johnohue.currencyconvert.foundation.MainActivity
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule : AndroidInjector<MainActivity> {

    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity
}