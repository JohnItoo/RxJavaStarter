package com.johnohue.currencyconvert

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import com.johnohue.currencyconvert.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MyApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .withApplicationContext(this)
            .withApplicationConfiguration(ApplicationConfigurationUtils())
            .build()
            .inject(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> =
        fragmentInjector

}