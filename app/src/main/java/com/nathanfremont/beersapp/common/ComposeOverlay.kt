package com.nathanfremont.beersapp.common

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nathanfremont.beersapp.ui.theme.BlackTransparent80

@Composable
fun ComposeOverlay(
    modifier: Modifier = Modifier,
    overlayClick: () -> Unit = {},
    overlayContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(BlackTransparent80)
            .fillMaxSize()
            .debounceClickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = overlayClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        overlayContent()
    }
}

@Composable
@Preview
private fun PreviewComposeOverlay() {
    ComposeOverlay {
        CircularProgressIndicator()
    }
}