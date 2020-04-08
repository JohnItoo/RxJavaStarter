package com.johnohue.currencyconvert.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.johnohue.currencyconvert.R
import com.johnohue.currencyconvert.databinding.FragmentCurrencyListBinding
import com.johnohue.currencyconvert.foundation.MainActivity
import com.johnohue.currencyconvert.utils.MiscUtils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CurrencyListFragment : Fragment(R.layout.fragment_currency_list) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CurrencyListViewModel
    private var currencyListBinding: FragmentCurrencyListBinding? = null

    protected val mainActivity: MainActivity
        get() {
            return activity as? MainActivity ?: throw IllegalStateException("Not attached!")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        this.mainActivity.setUpToolBar("Rates", true)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CurrencyListViewModel::class.java]
        mainActivity.showLoading()
        val binding = FragmentCurrencyListBinding.bind(view)
        currencyListBinding = binding
        currencyListBinding?.lifecycleOwner = this
        currencyListBinding?.viewModel = viewModel
        currencyListBinding?.routinesRecyclerView?.adapter =
            CurrencyExchangeAdapter(
                viewModel,
                MiscUtils()
            ).apply { setHasStableIds(true) }

        viewModel.hasError.observe(this, Observer {
            when (it) {
                true -> mainActivity.showError(R.string.cannot_poll_now_error)
                false -> mainActivity.dismissLoading()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        currencyListBinding = null
        super.onDestroyView()
    }
}