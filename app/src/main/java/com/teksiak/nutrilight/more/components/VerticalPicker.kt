package com.teksiak.nutrilight.more.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Typography
import com.teksiak.nutrilight.core.presentation.designsystem.White
import kotlin.math.abs
import kotlin.math.roundToInt

data class VerticalPickerStyle(
    val boxColor: Color = Primary,
    val selectedItemColor: Color = White,
    val outsideItemColor: Color = ShadedWhite,
    val textStyle: TextStyle = Typography.bodyLarge,
    val boxSize: DpSize = DpSize(125.dp, 40.dp),
)

@Composable
fun <T> VerticalPicker(
    items: List<T>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: VerticalPickerStyle = VerticalPickerStyle(),
) {
    val itemsCount = items.size
    val itemSize = with(LocalDensity.current) {
        style.boxSize.height.toPx()
    }
    val initialOffsetY = remember { selectedIndex * itemSize }

    var dragStartY by remember {
        mutableFloatStateOf(0f)
    }
    var currentOffsetY by remember {
        mutableFloatStateOf(initialOffsetY)
    }

    val animatedOffsetY by animateFloatAsState(
        targetValue = currentOffsetY,
        label = "animatedOffsetY"
    )

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .width(style.boxSize.width)
            .height(style.boxSize.height * 3)
            .alpha(0.99f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragStartY = it.y
                    },
                    onDrag = { change, dragAmount ->
                        val changeY = change.position.y
                        val newY = currentOffsetY + (dragStartY - changeY) * 0.1f

                        currentOffsetY = newY.coerceIn(
                            minimumValue = 0f,
                            maximumValue = (itemsCount - 1) * itemSize
                        )
                    },
                    onDragEnd = {
                        val rest = currentOffsetY % itemSize
                        val roundUp = abs(rest / itemSize).roundToInt() == 1
                        val newY = if (roundUp) {
                            if (rest > 0) currentOffsetY - rest + itemSize
                            else currentOffsetY - rest - itemSize
                        } else currentOffsetY - rest

                        currentOffsetY = newY.coerceIn(
                            minimumValue = 0f,
                            maximumValue = (itemsCount - 1) * itemSize
                        )

                        val selectedItemIndex = (currentOffsetY / itemSize).toInt()
                        if(selectedItemIndex != selectedIndex) {
                            onItemSelected(selectedItemIndex)
                        }
                    }
                )
            }
            .pointerInput(selectedIndex) {
                detectTapGestures(
                    onTap = { offset ->
                        if(offset.y < itemSize) {
                            val previousItemIndex = selectedIndex - 1
                            if(previousItemIndex >= 0) {
                                currentOffsetY = previousItemIndex * itemSize
                                onItemSelected(previousItemIndex)
                            }
                        } else if(offset.y > itemSize * 2) {
                            val nextItemIndex = selectedIndex + 1
                            if(nextItemIndex < itemsCount) {
                                currentOffsetY = nextItemIndex * itemSize
                                onItemSelected(nextItemIndex)
                            }
                        }
                    }
                )
            }
    ) {
        val boxHeight = style.boxSize.height.toPx()
        val boxCornerRadius = 20.dp.toPx()

        drawRoundRect(
            color = style.boxColor,
            topLeft = Offset(
                x = 0f,
                y = boxHeight
            ),
            size = Size(
                width = size.width,
                height = boxHeight
            ),
            cornerRadius = CornerRadius(boxCornerRadius),
        )

        for (i in 0 until itemsCount) {
            val currentY = i * boxHeight - animatedOffsetY + boxHeight

            val textLayoutResult = textMeasurer.measure(
                text = items[i].toString(),
                style = style.textStyle
            )

            translate(
                left = center.x - textLayoutResult.size.width / 2,
                top = currentY + textLayoutResult.size.height / 2
            ) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = items[i].toString(),
                    style = style.textStyle.copy(
                        color = style.selectedItemColor
                    ),
                )
            }
        }

        drawRect(
            color = style.outsideItemColor,
            topLeft = Offset(
                x = 0f,
                y = 0f
            ),
            size = Size(
                width = size.width,
                height = boxHeight
            ),
            blendMode = BlendMode.SrcIn
        )
        drawRect(
            color = style.outsideItemColor,
            topLeft = Offset(
                x = 0f,
                y = 2 * boxHeight
            ),
            size = Size(
                width = size.width,
                height = boxHeight
            ),
            blendMode = BlendMode.SrcIn
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalPickerPreview() {
    NutrilightTheme {
        VerticalPicker(
            items = listOf(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9"
            ),
            selectedIndex = 0,
            onItemSelected = {}
        )
    }
}
