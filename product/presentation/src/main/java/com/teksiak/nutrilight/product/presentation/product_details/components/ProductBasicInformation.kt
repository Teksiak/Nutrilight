package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.product.presentation.R
import com.teksiak.nutrilight.product.presentation.product_details.util.DummyProduct

@Composable
fun ProductBasicInformation(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicInformation(
            label = stringResource(id = R.string.brands),
            value = product.brands ?: "Unknown"
        )
        BasicInformation(
            label = stringResource(id = R.string.quantity),
            value = product.quantity ?: "Unknown"
        )
        BasicInformation(
            label = stringResource(id = R.string.packaging),
            value = product.packaging ?: "Unknown"
        )
    }
}

@Composable
private fun BasicInformation(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Silver,
            softWrap = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductBasicInformationPreview() {
    NutrilightTheme {
        ProductBasicInformation(
            product = DummyProduct
        )
    }
}