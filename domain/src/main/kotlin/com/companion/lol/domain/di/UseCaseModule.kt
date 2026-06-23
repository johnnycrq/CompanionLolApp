package com.companion.lol.domain.di

import com.companion.lol.domain.usecase.DeleteFavorites
import com.companion.lol.domain.usecase.DeleteSession
import com.companion.lol.domain.usecase.ObserveAuthenticatedEmailImpl
import com.companion.lol.domain.usecase.ObserveChampion
import com.companion.lol.domain.usecase.ObserveChampionDetailsImpl
import com.companion.lol.domain.usecase.ObserveSession
import com.companion.lol.domain.usecase.ObserveSettingsImpl
import com.companion.lol.domain.usecase.RefreshChampion
import com.companion.lol.domain.usecase.RefreshChampionDetails
import com.companion.lol.domain.usecase.UpdateFavorites
import com.companion.lol.domain.usecase.UpdateSession
import com.companion.lol.domain.usecase.UpdateSettings
import com.companion.lol.domain.usecase.impl.DeleteFavoritesImpl
import com.companion.lol.domain.usecase.impl.DeleteSessionImpl
import com.companion.lol.domain.usecase.impl.ObserveAuthenticatedEmail
import com.companion.lol.domain.usecase.impl.ObserveChampionDetails
import com.companion.lol.domain.usecase.impl.ObserveChampionImpl
import com.companion.lol.domain.usecase.impl.ObserveSessionImpl
import com.companion.lol.domain.usecase.impl.ObserveSettings
import com.companion.lol.domain.usecase.impl.RefreshChampionDetailsImpl
import com.companion.lol.domain.usecase.impl.RefreshChampionImpl
import com.companion.lol.domain.usecase.impl.UpdateFavoritesImpl
import com.companion.lol.domain.usecase.impl.UpdateSessionImpl
import com.companion.lol.domain.usecase.impl.UpdateSettingsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
  @Binds fun deleteFavorites(impl: DeleteFavoritesImpl): DeleteFavorites

  @Binds fun deleteSession(impl: DeleteSessionImpl): DeleteSession

  @Binds
  fun observeAuthenticatedEmail(impl: ObserveAuthenticatedEmailImpl): ObserveAuthenticatedEmail

  @Binds fun observeChampion(impl: ObserveChampionImpl): ObserveChampion

  @Binds fun observeChampionDetails(impl: ObserveChampionDetailsImpl): ObserveChampionDetails

  @Binds fun observeSession(impl: ObserveSessionImpl): ObserveSession

  @Binds fun observeSettings(impl: ObserveSettingsImpl): ObserveSettings

  @Binds fun refreshChampion(impl: RefreshChampionImpl): RefreshChampion

  @Binds fun refreshChampionDetails(impl: RefreshChampionDetailsImpl): RefreshChampionDetails

  @Binds fun updateFavorites(impl: UpdateFavoritesImpl): UpdateFavorites

  @Binds fun updateSession(impl: UpdateSessionImpl): UpdateSession

  @Binds fun updateSettings(impl: UpdateSettingsImpl): UpdateSettings
}
