package com.alicasts.december24.presentation.mocks

import com.alicasts.december24.R
import com.alicasts.december24.utils.StringResourceProvider

class FakeStringResourceProvider : StringResourceProvider {
    override fun getString(resourceId: Int): String {
        return when (resourceId) {
            R.string.null_string -> "Null"
            R.string.any_string -> "Qualquer"
            else -> "Fake String"
        }
    }
}