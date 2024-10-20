package com.teksiak.nutrilight.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.Primary
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.Silver
import com.teksiak.nutrilight.core.presentation.designsystem.White
import com.teksiak.nutrilight.core.presentation.getIcon
import com.teksiak.nutrilight.core.presentation.util.topBorder

@Composable
fun NutrilightNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .topBorder(1.dp, ShadedWhite)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationTab.entries.forEach { navTab ->
            NavigationTab(
                navTab = navTab,
                isSelected = navTab == currentTab,
                onClick = {
                    onTabSelected(navTab)
                }
            )
        }
    }
}

@Composable
private fun NavigationTab(
    navTab: NavigationTab,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .width(60.dp)
            .then(
                if (!isSelected)
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(
                            color = Silver,
                            bounded = true,
                            radius = 30.dp
                        )
                    ) {
                        onClick()
                    }
                else Modifier
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = navTab.getIcon()
            ),
            contentDescription = navTab.toString(),
            tint = if (isSelected) Primary else Silver
        )
        Text(
            text = navTab.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) Primary else Silver
        )
    }
}

@Preview
@Composable
private fun NutrilightNavBarPreview() {
    NutrilightNavBar(
        currentTab = NavigationTab.Home
    )
}