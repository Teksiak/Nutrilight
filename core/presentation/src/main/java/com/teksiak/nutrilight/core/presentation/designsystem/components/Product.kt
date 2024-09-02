package com.teksiak.nutrilight.core.presentation.designsystem.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.White

@Composable
fun Product(
    product: Product,
    isFavourite: Boolean = false,
    onFavouriteToggle: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = ShadedWhite)
            .background(color = White)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color = ShadedWhite)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            product.brands?.let { brands ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = brands,
                    style = MaterialTheme.typography.bodySmall,
                    color = Silver,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            product.nutriments?.energyKcal?.let { energyKcal ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$energyKcal kcal / 100g",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp),
            horizontalAlignment = Alignment.End
        ) {
            FavouriteButton(
                isFavourite = isFavourite,
                onToggle = onFavouriteToggle
            )
            Spacer(modifier = Modifier.weight(1f))
            product.score?.let { score ->
                NutrilightScore(
                    size = NutrilightScoreSize.Small,
                     score = score
                )
            }
        }
    }
}

@Preview(
    heightDp = 96,
    widthDp = 344
)
@Composable
fun ProductPreview() {
    Product(
        product =  ProductDummy,
        isFavourite = true
    )
}

private val ProductDummy =  Product(
    code = "20724696",
    name = "Californian Almond test",
    brands = "Alesto,Lidl,Solent",
    quantity = "200g",
    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
    novaGroup = NovaGroup.NOVA_1,
    nutriments = Nutriments(
        energyKj = 2567,
        energyKcal = 621,
        fat = 53.3f,
        saturatedFat = 4.3f,
        carbohydrates = 4.8f,
        sugars = 4.8f,
        fiber = 12.1f,
        protein = 24.5f,
        salt = 0.01f
    ),
    allergens = listOf(
        "Nuts"
    ),
    ingredients = listOf(
        "almonds"
    ),
    score = 4.6f
)