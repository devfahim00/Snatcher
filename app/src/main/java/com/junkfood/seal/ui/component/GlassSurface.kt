package com.junkfood.seal.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.junkfood.seal.ui.theme.CornerRadius
import com.junkfood.seal.ui.theme.GlassAlpha
import com.junkfood.seal.ui.theme.glassColors

/**
 * Lightweight frosted-glass container used by the refreshed surfaces.
 *
 * The effect is intentionally cheap: a translucent surface container plus a hairline border. It
 * reads as "glass" on both light and dark themes without paying the cost of a real blur pass, which
 * keeps scrolling smooth on low-end devices.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = CornerRadius.largeShape,
    strong: Boolean = false,
    borderWidth: Dp = 1.dp,
    containerColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = glassColors(strong = strong)
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor ?: colors.container,
        border = BorderStroke(borderWidth, colors.border),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Box(modifier = Modifier.padding(contentPadding), content = content)
    }
}

/** Convenience full-width glass card with the standard horizontal inset used across pages. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = CornerRadius.mediumShape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = shape,
        contentPadding = contentPadding,
        content = content,
    )
}

/** Pill-shaped glass chip, used for the floating navigation bar background. */
@Composable
fun GlassPill(
    modifier: Modifier = Modifier,
    strong: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    GlassSurface(
        modifier = modifier,
        shape = RoundedCornerShape(CornerRadius.pill),
        strong = strong,
        containerColor =
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                alpha = if (strong) GlassAlpha.SurfaceStrong else GlassAlpha.Surface
            ),
        content = content,
    )
}
