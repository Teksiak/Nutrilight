package com.teksiak.nutrilight.more

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teksiak.nutrilight.R
import com.teksiak.nutrilight.core.presentation.NavigationTab
import com.teksiak.nutrilight.core.presentation.designsystem.LogoIcon
import com.teksiak.nutrilight.core.presentation.designsystem.ShadedWhite
import com.teksiak.nutrilight.core.presentation.designsystem.components.NutrilightScaffold
import com.teksiak.nutrilight.core.presentation.util.bottomBorder
import kotlinx.serialization.Serializable

@Serializable
object MoreRoute

@Composable
fun MoreScreenRoot(
    viewModel: MoreViewModel,
    onNavigateBack: () -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    BackHandler {
        onNavigateBack()
    }

    HomeScreen(
        onAction = { action ->
            when(action) {
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateWithTab = navigateWithTab
    )
}

@Composable
private fun HomeScreen(
    onAction: (MoreAction) -> Unit,
    navigateWithTab: (NavigationTab) -> Unit
) {
    NutrilightScaffold(
        topAppBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(
                        strokeWidth = 1.dp,
                        color = ShadedWhite
                    )
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.nutrilight),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.shining_light),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Image(
                    imageVector = LogoIcon,
                    contentDescription = stringResource(R.string.logo),
                )
            }
        },
        currentTab = NavigationTab.More,
        onTabSelected = navigateWithTab
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}