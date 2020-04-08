package com.johnohue.currencyconvert.di

import android.app.Application
import com.johnohue.currencyconvert.MyApplication
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        APIServiceModule::class,
        RepositoryModule::class]
)
interface ApplicationComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withApplicationConfiguration(applicationConfiguration: ApplicationConfigurationUtils): Builder

        @BindsInstance
        fun withApplicationContext(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(currencyExchangeApplication: MyApplication)
}