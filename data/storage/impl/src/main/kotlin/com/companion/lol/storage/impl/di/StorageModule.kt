package com.companion.lol.storage.impl.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.companion.lol.core.model.GridSize
import com.companion.lol.core.model.PartyType
import com.companion.lol.core.model.SortOrder
import com.companion.lol.storage.impl.adapter.ChampionIdAdapter
import com.companion.lol.storage.impl.adapter.ChampionTagAdapter
import com.companion.lol.storage.impl.adapter.PartyTypeIdAdapter
import com.companion.lol.storage.impl.adapter.SingleIdAdapter
import com.companion.lol.storage.impl.adapter.SkinIdAdapter
import com.companion.lol.storage.impl.util.DatabaseContext
import com.companion.lol.storage.impl.util.DatabaseContextImpl
import com.companion.lol.storage.impl.util.DatabaseTransacter
import com.companion.lol.storage.impl.util.DatabaseTransacterImpl
import com.companion.lol.storage.sqldelight.LolAppDb
import com.companion.lol.storage.sqldelight.tables.ChampionDetailsTable
import com.companion.lol.storage.sqldelight.tables.ChampionFavoritesTable
import com.companion.lol.storage.sqldelight.tables.ChampionPartyTypeTable
import com.companion.lol.storage.sqldelight.tables.ChampionTable
import com.companion.lol.storage.sqldelight.tables.SingletonSessionTable
import com.companion.lol.storage.sqldelight.tables.SingletonSettingsTable
import com.companion.lol.storage.sqldelight.tables.SkinTable
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

  @Binds @Singleton abstract fun transacter(impl: DatabaseTransacterImpl): DatabaseTransacter

  @Binds @Singleton abstract fun databaseContext(impl: DatabaseContextImpl): DatabaseContext

  companion object {
    @Provides
    @Singleton
    fun driver(@ApplicationContext context: Context): SqlDriver =
      AndroidSqliteDriver(
        schema = LolAppDb.Schema,
        context = context,
        name = "com.lol.storage.db",
        callback =
          object : AndroidSqliteDriver.Callback(LolAppDb.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
              super.onOpen(db)

              db.query("PRAGMA journal_mode=WAL;").close()
              db.query("PRAGMA synchronous=NORMAL;").close()
              db.execSQL("PRAGMA temp_store=MEMORY;")
              db.execSQL("PRAGMA cache_size=-6000;")
            }
          },
      )

    @Provides
    @Singleton
    fun database(driver: SqlDriver): LolAppDb =
      LolAppDb(
        driver = driver,
        ChampionTableAdapter = ChampionTable.Adapter(ChampionIdAdapter, PartyTypeIdAdapter),
        ChampionFavoritesTableAdapter = ChampionFavoritesTable.Adapter(ChampionIdAdapter),
        ChampionPartyTypeTableAdapter =
          ChampionPartyTypeTable.Adapter(PartyTypeIdAdapter, EnumColumnAdapter<PartyType>()),
        SingletonSettingsTableAdapter =
          SingletonSettingsTable.Adapter(
            SingleIdAdapter,
            EnumColumnAdapter<GridSize>(),
            EnumColumnAdapter<SortOrder>(),
          ),
        SingletonSessionTableAdapter = SingletonSessionTable.Adapter(SingleIdAdapter),
        SkinTableAdapter = SkinTable.Adapter(ChampionIdAdapter, SkinIdAdapter, IntColumnAdapter),
        ChampionDetailsTableAdapter =
          ChampionDetailsTable.Adapter(
            idAdapter = ChampionIdAdapter,
            tagsAdapter = ChampionTagAdapter,
          ),
      )
  }
}
