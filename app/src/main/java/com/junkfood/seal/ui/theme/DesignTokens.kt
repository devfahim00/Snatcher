package com.junkfood.seal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Central design-token layer for the refreshed UI.
 *
 * Everything the new surfaces (glass cards, skeleton loaders, floating pill navigation) need is
 * declared here so screens never hard-code magic dp/alpha values.
 */
object Spacing {
    val extraSmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 12.dp
    val large: Dp = 16.dp
    val extraLarge: Dp = 24.dp
    val huge: Dp = 32.dp
}

object CornerRadius {
    val small: Dp = 12.dp
    val medium: Dp = 18.dp
    val large: Dp = 24.dp
    val pill: Dp = 28.dp

    val smallShape = RoundedCornerShape(small)
    val mediumShape = RoundedCornerShape(medium)
    val largeShape = RoundedCornerShape(large)
    val pillShape = RoundedCornerShape(pill)
}

object MotionDuration {
    /** Shimmer sweep for skeleton placeholders. */
    const val Shimmer = 1200

    /** Cross-fade between loading skeleton and real content. */
    const val ContentSwap = 320

    /** Pill navbar indicator travel. */
    const val NavIndicator = 300
}

/**
 * Alpha values that produce the frosted "glass" look. Kept low so the effect stays subtle and
 * legible on both light and dark themes.
 */
object GlassAlpha {
    const val Surface = 0.62f
    const val SurfaceStrong = 0.78f
    const val Border = 0.35f
    const val SkeletonBase = 0.10f
    const val SkeletonHighlight = 0.22f
}

/** Resolved glass colours for the current theme. */
data class GlassColors(
    val container: Color,
    val border: Color,
    val content: Color,
)

@Composable
@ReadOnlyComposable
fun glassColors(strong: Boolean = false): GlassColors {
    val scheme = MaterialTheme.colorScheme
    return GlassColors(
        container =
            scheme.surfaceContainerHigh.copy(
                alpha = if (strong) GlassAlpha.SurfaceStrong else GlassAlpha.Surface
            ),
        border = scheme.outlineVariant.copy(alpha = GlassAlpha.Border),
        content = scheme.onSurface,
    )
}
