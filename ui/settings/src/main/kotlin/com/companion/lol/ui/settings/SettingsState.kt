package com.companion.lol.ui.settings

import com.companion.lol.core.ui.state.ComposeState

data class SettingsState(val emailAddress: String, val autoSync: Boolean) : ComposeState
