package com.lol.app.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.Size
import coil3.transform.Transformation
import com.companion.lol.data.DdragonImage
import com.companion.lol.storage.impl.model.ids.ChampionId

@Composable
fun DominantColorCoilImage(
    modifier: Modifier,
    image: DdragonImage,
    placeholderColor: Color = MaterialTheme.colorScheme.primary,
    updatePalette: Boolean = false,
    imageModifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(image.imageUrl)
            .dominantColorTransformation(
                championId = image.championId,
                enabled = updatePalette
            ).build()
    )

    val painterState by painter.state.collectAsState()

    val animatedColor = animateColorAsState(
        targetValue = when (painterState) {
            is AsyncImagePainter.State.Success -> Color.Transparent
            else -> ChampionColorCache.getColor(image.championId).value
                ?: placeholderColor
        },
        label = "",
        animationSpec = tween(350)
    )

    Box(
        modifier = modifier
            .drawBehind { drawRect(animatedColor.value) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .then(imageModifier),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

private fun ImageRequest.Builder.dominantColorTransformation(
    championId: ChampionId,
    enabled: Boolean
): ImageRequest.Builder {
    if (!enabled) return this
    // if we have a color, we are done. No transformation needed!
    val cachedColor = ChampionColorCache.getColor(championId)
    if (cachedColor.value != null) return this

    return this.transformations(
        object: Transformation(){
            override val cacheKey: String = championId.value.toString()

            override suspend fun transform(
                input: coil3.Bitmap,
                size: Size
            ): coil3.Bitmap {
                Palette.from(input)
                    .generate()
                    .let {
                        ChampionColorCache.putColor(
                            id = championId,
                            color = Color(
                                it.getVibrantColor(0)
                                    .takeIf { color -> color != 0 }
                                    ?: it.getDominantColor(0)
                            )
                        )
                    }

                return input
            }
        }
    )
}