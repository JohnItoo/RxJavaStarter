package com.johnohue.currencyconvert.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.johnohue.currencyconvert.core.repository.CurrencyRepository
import com.johnohue.currencyconvert.models.CurrencyRateItem
import com.johnohue.currencyconvert.models.CurrencyRatesResponse
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class CurrencyListViewModelTest {
    private lateinit var mockCurrencyListRepository: CurrencyRepository
    private lateinit var viewModel: CurrencyListViewModel
    private lateinit var schedulerProvider: BaseSchedulerProvider

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        schedulerProvider = TrampolineSchedulerProvider()
        RxJavaPlugins.setIoSchedulerHandler { schedulerProvider.ui() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { schedulerProvider.ui() }
        mockCurrencyListRepository = mock(CurrencyRepository::class.java)
        viewModel = CurrencyListViewModel(mockCurrencyListRepository, schedulerProvider)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `get Currency List With Proper Base Currency Passes `() {
        `when`(mockCurrencyListRepository.getCurrencyRates("EUR")).thenReturn(
            Observable.just(
                CurrencyRatesResponse(
                    "EUR",
                    hashMapOf("ABC" to 222.2)
                )
            )
        )
        viewModel.onResume()
        assertThat(viewModel.currencies.value)
            .containsExactly(
                CurrencyRateItem("EUR", 10.0,true),
                CurrencyRateItem("ABC", 2222.0)
            )
        assertFalse(viewModel.hasError.value!!)
    }

    @Test
    fun `get Currency List With Wrong Base Currency Fails`() {
        `when`(mockCurrencyListRepository.getCurrencyRates("USD")).thenReturn(
            Observable.just(
                CurrencyRatesResponse(
                    "USD",
                    hashMapOf("ABC" to 222.2)
                )
            )
        )
        viewModel.onResume()
        assertThat(viewModel.currencies.value)
            .isEmpty()
        assertTrue(viewModel.hasError.value!!)
    }

    @Test
    fun `List emitter throws error and error dialog shows Successfully Fails`() {
        `when`(mockCurrencyListRepository.getCurrencyRates("EUR")).thenReturn(
            Observable.error(Throwable("An error occured"))
        )
        viewModel.onResume()
        assertTrue(viewModel.hasError.value!!)
    }

    @Test
    fun `Item Click on List Changes Base Currency Passes`() {
        setupExpectationForEmmitables()
        viewModel.onResume()
        viewModel.onItemClicked(CurrencyRateItem("ABC", 250.35))

        assertThat(viewModel.currencies.value).containsExactly(
            CurrencyRateItem("ABC", 250.35, true),
            CurrencyRateItem("USD", 375.525)
        )
        assertFalse(viewModel.hasError.value!!)
    }

    @Test
    fun `edit Text Editable Change calls on Base Amount Change Passes`() {
        setupExpectationForEmmitables()
        viewModel.onResume()
        viewModel.onCurrencyAmountChanged("200")

        assertThat(viewModel.currencies.value).containsExactly(
            CurrencyRateItem("EUR", 200.0, true),
            CurrencyRateItem("USD", 300.0)
        )
    }

    private fun setupExpectationForEmmitables()  {
        `when`(mockCurrencyListRepository.getCurrencyRates("ABC")).thenReturn(
            Observable.just(
                CurrencyRatesResponse(
                    "ABC",
                    hashMapOf("USD" to 1.5)
                )
            )
        )

        `when`(mockCurrencyListRepository.getCurrencyRates("EUR")).thenReturn(
            Observable.just(
                CurrencyRatesResponse(
                    "EUR",
                    hashMapOf("USD" to 1.5)
                )
            )
        )
    }
}