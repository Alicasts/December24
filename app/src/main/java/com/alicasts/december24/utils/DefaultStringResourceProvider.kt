package com.alicasts.december24.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultStringResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : StringResourceProvider {
    override fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }
}