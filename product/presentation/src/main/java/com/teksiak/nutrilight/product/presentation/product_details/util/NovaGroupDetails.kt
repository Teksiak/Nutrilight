package com.teksiak.nutrilight.product.presentation.product_details.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.product.presentation.R

data class NovaGroupDetails(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)

val NovaGroup.details: NovaGroupDetails
    get() {
        return when(this) {
            NovaGroup.NOVA_1 -> NovaGroupDetails(
                image = R.drawable.nova_1,
                title = R.string.nova_1_title,
                description = R.string.nova_1_description
            )
            NovaGroup.NOVA_2 -> NovaGroupDetails(
                image = R.drawable.nova_2,
                title = R.string.nova_2_title,
                description = R.string.nova_2_description
            )
            NovaGroup.NOVA_3 -> NovaGroupDetails(
                image = R.drawable.nova_3,
                title = R.string.nova_3_title,
                description = R.string.nova_3_description
            )
            NovaGroup.NOVA_4 -> NovaGroupDetails(
                image = R.drawable.nova_4,
                title = R.string.nova_4_title,
                description = R.string.nova_4_description
            )
        }
    }