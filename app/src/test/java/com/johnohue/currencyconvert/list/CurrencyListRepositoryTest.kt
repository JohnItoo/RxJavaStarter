package com.johnohue.currencyconvert.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.johnohue.currencyconvert.core.network.CurrencyService
import com.johnohue.currencyconvert.core.repository.CurrencyRepository
import com.johnohue.currencyconvert.core.repository.CurrencyRepositoryImpl
import com.johnohue.currencyconvert.models.BaseCurrency
import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import com.johnohue.currencyconvert.utils.ApplicationConfigurationUtils
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit


@RunWith(JUnit4::class)
class CurrencyListRepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var currencyServiceMock: CurrencyService
    private lateinit var repository: CurrencyRepository
    private lateinit var baseCurrency: BaseCurrency
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        currencyServiceMock = mock(CurrencyService::class.java)
        baseCurrency = mock(BaseCurrency::class.java)
        repository = CurrencyRepositoryImpl(ApplicationConfigurationUtils(), currencyServiceMock)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `poll new currency rates every second`() {
        val observer = TestObserver<CurrencyRatesResponse>()

        val defaultResponse = CurrencyRatesResponse("GBP", hashMapOf("NGR" to 1.2345))

        getSingleEmmiterFromUSD(defaultResponse)

        repository.getCurrencyRates("USD").subscribe(observer)
        observer.awaitCount(1)
        observer.assertValue(defaultResponse)

        val eurosToGBP = CurrencyRatesResponse(
            "EUR",
            hashMapOf("GBP" to 3.44)
        )

       getSingleEmmiterFromUSD(eurosToGBP)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.awaitCount(2)
        observer.assertValues(defaultResponse, eurosToGBP).assertNoErrors()
    }

    fun getSingleEmmiterFromUSD(response: CurrencyRatesResponse) =  `when`(currencyServiceMock.getCurrencyRates("USD")).thenReturn(
        Single.just(response)
    )
}
