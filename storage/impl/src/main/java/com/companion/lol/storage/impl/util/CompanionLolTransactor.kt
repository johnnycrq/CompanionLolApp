package com.companion.lol.storage.impl.util

import app.cash.sqldelight.Transacter
import com.companion.lol.storage.sqldelight.LolAppDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionLolTransactor @Inject constructor(
    private val db: LolAppDb
): Transacter by db