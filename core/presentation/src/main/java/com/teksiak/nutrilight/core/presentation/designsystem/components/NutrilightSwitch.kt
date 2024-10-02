package com.teksiak.nutrilight.core.presentation.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.Secondary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun NutrilightSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var composed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch {
            delay(100)
            composed = true
        }
    }

    val thumbColor by animateColorAsState(
        if (checked) Secondary else ShadedWhite,
        animationSpec = tween(
            durationMillis = if(composed) 300 else 0,
            easing = FastOutSlowInEasing
        )
    )
    val trackColor by animateColorAsState(
        if (checked) Primary else Silver,
        animationSpec = tween(
            durationMillis = if(composed) 200 else 0,
            delayMillis = if(composed) 100 else 0,
            easing = FastOutSlowInEasing
        )
    )
    val thumbOffset by animateFloatAsState(
        if (checked) 1f else 0f,
        animationSpec = tween(
            durationMillis = if(composed) 300 else 0,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = modifier
            .size(width = 64.dp, height = 40.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onCheckedChange(!checked) }
            .border(
                width = 1.dp,
                color = ShadedWhite,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(trackColor)
        )
        Box(
            modifier = Modifier
                .offset(
                    x = (thumbOffset * 22).dp,
                )
                .size(24.dp)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NutrilightSwitchPreview() {
    NutrilightTheme {
        val state = remember { mutableStateOf(true) }
        NutrilightSwitch(
            checked = state.value,
            onCheckedChange = { state.value = it }
        )
    }
}