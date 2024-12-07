package com.alicasts.december24.utils

import android.content.Context
import com.alicasts.december24.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class StringProvider @Inject constructor(
    @ApplicationContext private val context: Context?
) {
    private var anyString: String? = null
    private var nullString: String? = null

    constructor(anyString: String, nullString: String) : this(context = null) {
        this.anyString = anyString
        this.nullString = nullString
    }

    fun getAnyString(): String {
        return anyString ?: context?.getString(R.string.qualquer) ?: "DefaultAny"
    }

    fun getNullString(): String {
        return nullString ?: context?.getString(R.string.nulo) ?: "DefaultNull"
    }
}