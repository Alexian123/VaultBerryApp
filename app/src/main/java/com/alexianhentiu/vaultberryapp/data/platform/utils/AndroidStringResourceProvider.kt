package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.content.Context
import androidx.annotation.StringRes
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidStringResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : StringResourceProvider {

    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(@StringRes resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}