package com.lol.app.io

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Singleton
class AppScope @Inject constructor() :
  CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Main)
