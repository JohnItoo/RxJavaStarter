package com.johnohue.currencyconvert.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.johnohue.currencyconvert.R
import com.squareup.picasso.Picasso
import java.util.Currency

class MiscUtils {
    fun loadFlagIntoView(currency : String, context: Context, imageView: ImageView, textView: TextView) {
        val currencyItem = makeCurrency(currency)
        textView.text = currencyItem.displayName
       Picasso.get()
            .load(context.resources.getIdentifier(currencyItem .currencyCode.toLowerCase().plus(context.getString(
                R.string.flag)),"drawable", context.applicationInfo.packageName))
            .into(imageView)
    }

    private fun makeCurrency(short: String) : Currency = Currency.getInstance(short)
}