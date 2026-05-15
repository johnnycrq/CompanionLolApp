package com.lol.app.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.lol.app.base.theme.CompanionAppTheme

@Preview
//@Preview(uiMode = UI_MODE_NIGHT_YES)
annotation class CompanionAppPreview
class CompanionAppPreviewWrapperProvider : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable () -> Unit) {
        // Wrap the content in a Material3 Scaffold to provide a standard app structure
        CompanionAppTheme {
            content()
        }
    }
}