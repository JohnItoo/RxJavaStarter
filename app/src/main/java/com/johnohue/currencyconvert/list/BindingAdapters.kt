package com.johnohue.currencyconvert.list

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johnohue.currencyconvert.models.CurrencyRateItem

@BindingAdapter("currencyList")
fun bindRoutinesRecyclerView(recyclerView: RecyclerView, data: List<CurrencyRateItem>?) {
    data?.let {
        val adapter = recyclerView.adapter as CurrencyExchangeAdapter
        adapter.submitList(data)
    }
}
