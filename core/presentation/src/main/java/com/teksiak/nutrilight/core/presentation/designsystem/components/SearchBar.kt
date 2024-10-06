package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.designsystem.NutrilightTheme
import com.teksiak.nutrilight.core.presentation.designsystem.ScanBarIcon
import com.teksiak.nutrilight.core.presentation.designsystem.SearchIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.TintedBlack
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.designsystem.XIcon

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onScanBarClick: () -> Unit,
    onClick: () -> Unit = {},
    focusOnComposition: Boolean = false,
) {
    var isSearchFocused by remember { mutableStateOf(false) }
    val isTyping = if(isSearchFocused) {
        searchValue.isNotEmpty() && isSearchFocused
    } else searchValue.isNotEmpty()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
    ) {
        SearchInput(
            value = searchValue,
            onValueChange = onSearchValueChange,
            modifier = Modifier
                .animateContentSize()
                .weight(1f),
            onSearch = onSearch,
            onClear = onClear,
            onFocusChanged = { isFocused ->
                isSearchFocused = isFocused
                if(isFocused) {
                    onClick()
                }
            },
            focusOnComposition = focusOnComposition
        )
        AnimatedVisibility(
            visible = !isTyping,
            enter = expandHorizontally(
                animationSpec = tween(300),
                expandFrom = Alignment.Start
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(300),
                shrinkTowards = Alignment.Start
            )
        ) {
            CircleButton(
                icon = ScanBarIcon,
                onClick = onScanBarClick,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onFocusChanged: (Boolean) -> Unit = { },
    focusOnComposition: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        if(focusOnComposition) {
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(40.dp)
            .background(color = White)
            .border(width = 1.dp, color = ShadedWhite, shape = RoundedCornerShape(size = 20.dp))
            .padding(8.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) },
        textStyle = MaterialTheme.typography.bodyLarge,
        cursorBrush = SolidColor(TintedBlack),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(value)
                focusManager.clearFocus()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = SearchIcon,
                    modifier = Modifier
                        .size(24.dp),
                    tint = TintedBlack,
                    contentDescription = null
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if(value.isEmpty()) {
                        Text(
                            text = "Search for a product",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Silver
                        )
                    }
                    innerTextField()
                }
                AnimatedVisibility(visible = value.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = onClear
                    ) {
                        Icon(
                            imageVector = XIcon,
                            modifier = Modifier
                                .size(24.dp),
                            tint = TintedBlack,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}


@Preview(
    showBackground = true,
    widthDp = 345,
    heightDp = 40
)
@Composable
fun SearchBarPreview() {
    NutrilightTheme {
        SearchBar(
            searchValue = "",
            onSearchValueChange = {},
            onSearch = {},
            onScanBarClick = {},
            onClear = {}
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 345,
    heightDp = 40
)
@Composable
fun SearchBarPreviewFilled() {
    NutrilightTheme {
        SearchBar(
            searchValue = "Nutella",
            onSearchValueChange = {},
            onSearch = {},
            onScanBarClick = {},
            onClear = {}
        )
    }
}