package com.johnohue.currencyconvert.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johnohue.currencyconvert.R
import com.johnohue.currencyconvert.databinding.ItemCurrencyListBinding
import com.johnohue.currencyconvert.models.CurrencyRateItem
import com.johnohue.currencyconvert.utils.CurrencyRateValueConverter
import com.johnohue.currencyconvert.utils.MiscUtils
import com.johnohue.currencyconvert.utils.TextWatcherImpl

class CurrencyExchangeAdapter(
    private val viewModel: CurrencyListViewModel,
    private val miscUtils: MiscUtils
) :
    ListAdapter<CurrencyRateItem, CurrencyExchangeAdapter.CurrencyExchangeViewHolder>(DiffCallback()) {
    val textWatcher = TextWatcherImpl(viewModel)

    inner class CurrencyExchangeViewHolder(private var binding: ItemCurrencyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyItem: CurrencyRateItem, viewModel: CurrencyListViewModel) {
            binding.editText.removeTextChangedListener(textWatcher)
            binding.viewModel = viewModel
            binding.currencyRateItem = currencyItem
            if (currencyItem.editable) {
                binding.editText.addTextChangedListener(textWatcher)
            }
            miscUtils.loadFlagIntoView(
                currencyItem.name,
                binding.root.context,
                binding.currencyFlag,
                binding.currencyFull
            )
            binding.executePendingBindings()
        }

        fun bindAmount(amount: Double) {
            val textToSet = CurrencyRateValueConverter.doubleToString(amount)
            if (binding.editText.text.toString() != textToSet) {
                val selectionStart = binding.editText.selectionStart
                binding.editText.setText(textToSet)
                if (selectionStart <= textToSet.length) {
                    binding.editText.setSelection(selectionStart)
                } else {
                    binding.editText.setSelection(textToSet.length)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyExchangeViewHolder {
        val holder = CurrencyExchangeViewHolder(
            ItemCurrencyListBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_currency_list,
                    parent,
                    false
                )
            )
        )
        return holder
    }

    override fun onBindViewHolder(
        holder: CurrencyExchangeViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bindAmount(payloads[0] as Double)
        }
    }

    override fun onBindViewHolder(holder: CurrencyExchangeViewHolder, position: Int) =
        holder.bind(getItem(position), viewModel)

    override fun getItemId(position: Int): Long = getItem(position).name.hashCode().toLong()
}

class DiffCallback : DiffUtil.ItemCallback<CurrencyRateItem>() {

    override fun areItemsTheSame(
        oldItem: CurrencyRateItem,
        newItem: CurrencyRateItem
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CurrencyRateItem,
        newItem: CurrencyRateItem
    ): Boolean =
        oldItem.name == newItem.name

    override fun getChangePayload(oldItem: CurrencyRateItem, newItem: CurrencyRateItem): Any? {
        return newItem.value
    }
}