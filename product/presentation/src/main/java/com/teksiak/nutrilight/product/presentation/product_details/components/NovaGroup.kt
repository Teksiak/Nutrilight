package com.teksiak.nutrilight.product.presentation.product_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.product.presentation.product_details.util.details

@Composable
fun NovaGroup(
    novaGroup: NovaGroup,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = ShadedWhite,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(35.dp)
                .height(60.dp),
            painter = painterResource(id = novaGroup.details.image),
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(novaGroup.details.title),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(novaGroup.details.description),
                style = MaterialTheme.typography.bodyMedium,
                color = Silver,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NovaGroupPreview() {
    NutrilightTheme {
        NovaGroup(novaGroup = NovaGroup.NOVA_1)
    }
}