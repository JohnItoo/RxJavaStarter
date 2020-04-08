package com.johnohue.currencyconvert.utils

import android.text.Editable
import android.text.TextWatcher
import com.johnohue.currencyconvert.list.CurrencyListViewModel

class TextWatcherImpl(
                      private val viewModel: CurrencyListViewModel) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        viewModel.onCurrencyAmountChanged(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {}

}