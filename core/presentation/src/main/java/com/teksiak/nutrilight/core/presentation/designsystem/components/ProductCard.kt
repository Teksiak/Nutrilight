package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.HeartFilledIcon
import com.teksiak.nutrilight.core.presentation.designsystem.HeartIcon
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.ui_models.ProductUi
import com.teksiak.nutrilight.core.presentation.ui_models.toProductUi
import com.teksiak.nutrilight.core.presentation.util.DummyProduct

@Composable
fun ProductCard(
    productUi: ProductUi,
    modifier: Modifier = Modifier,
    onFavouriteToggle: (String) -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(97.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = ShadedWhite, shape = RoundedCornerShape(16.dp))
            .background(color = White)
            .clickable { onNavigate(productUi.code) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductImage(
            imageUrl = if(productUi.showImage) productUi.smallImageUrl else null,
            modifier = Modifier
                .sizeIn(maxWidth = 72.dp, minWidth = 72.dp, maxHeight = 72.dp, minHeight = 72.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(4.dp)),
            loadingSize = 24.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = productUi.name,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(2.dp))
            productUi.brands.takeIf { it.isNotBlank() }?.let { brands ->
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
            productUi.nutrimentsUi?.roundedEnergyKcal?.let { energyKcal ->
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
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onFavouriteToggle(productUi.code)
                    }
                ,
                imageVector = if(productUi.isFavourite) HeartFilledIcon else HeartIcon,
                contentDescription = if(productUi.isFavourite) "Remove from favourites" else "Add to favourites",
                tint = if(productUi.isFavourite) Color.Unspecified else Silver
            )
            Spacer(modifier = Modifier.weight(1f))
            productUi.score?.let { score ->
                NutrilightScore(
                    size = NutrilightScoreSize.Small,
                     score = score
                )
            }
        }
    }
}

@Preview(
    widthDp = 344
)
@Composable
fun ProductPreview() {
    NutrilightTheme {
        ProductCard(
            productUi =  DummyProduct.toProductUi(),
        )
    }
}