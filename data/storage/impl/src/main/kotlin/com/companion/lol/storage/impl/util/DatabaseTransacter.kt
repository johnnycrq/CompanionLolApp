package com.companion.lol.storage.impl.util

import app.cash.sqldelight.Transacter
import com.companion.lol.storage.sqldelight.LolAppDb
import javax.inject.Inject
import javax.inject.Singleton

interface DatabaseTransacter : Transacter

@Singleton
class DatabaseTransacterImpl @Inject constructor(app: LolAppDb) :
  DatabaseTransacter, Transacter by app
