package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teksiak.nutrilight.core.presentation.designsystem.FjallaOne
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ScoreIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ScoreSmallIcon
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack

enum class NutrilightScoreSize {
    Small,
    Default
}

@Composable
fun NutrilightScore(
    score: Float,
    size: NutrilightScoreSize = NutrilightScoreSize.Default
) {
    val width = if(size == NutrilightScoreSize.Default) 76.dp else 36.dp
    val height = if(size == NutrilightScoreSize.Default) 71.dp else 34.dp
    val fontSize = if(size == NutrilightScoreSize.Default) 36.sp else 17.sp
    val icon = if(size == NutrilightScoreSize.Default) ScoreIcon else ScoreSmallIcon
    val startPadding = if(size == NutrilightScoreSize.Default) 12.dp else 5.5.dp

    Box(
        modifier = Modifier
            .size(width = width, height),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            imageVector = icon,
            contentDescription = "Rating"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = startPadding),
            text = score.toString(),
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = FjallaOne,
                color = TintedBlack,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutrilightScorePreview() {
    NutrilightTheme {
        NutrilightScore(score = 4.6f)
    }
}

@Preview(showBackground = true)
@Composable
fun NutrilightScoreSmallPreview() {
    NutrilightTheme {
        NutrilightScore(
            score = 4.6f,
            size = NutrilightScoreSize.Small
        )
    }
}