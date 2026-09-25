package com.junkfood.seal.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.junkfood.seal.ui.theme.CornerRadius
import com.junkfood.seal.ui.theme.GlassAlpha
import com.junkfood.seal.ui.theme.glassColors

/**
 * Frosted-glass container used across Snatcher's surfaces.
 *
 * The effect stays cheap on purpose: a translucent surface container, a hairline gradient border
 * and a soft diagonal sheen overlay that reads as light catching the top edge of the glass. No
 * real blur pass is involved, which keeps scrolling smooth on low-end devices while still giving
 * the unmistakable glassy look on both light and dark themes.
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
    val borderBrush =
        Brush.linearGradient(
            colors =
                listOf(
                    Color.White.copy(alpha = GlassAlpha.SheenTop),
                    colors.border,
                    Color.White.copy(alpha = GlassAlpha.SheenTop / 2),
                ),
        )
    val sheenBrush =
        Brush.linearGradient(
            colors =
                listOf(
                    Color.White.copy(alpha = GlassAlpha.Sheen),
                    Color.White.copy(alpha = GlassAlpha.Sheen / 3),
                    Color.Transparent,
                ),
        )
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor ?: colors.container,
        border = BorderStroke(borderWidth, borderBrush),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
            // Diagonal sheen sitting on top of the content, clipped to the surface shape.
            Box(
                modifier =
                    Modifier.matchParentSize()
                        .clip(shape)
                        .background(sheenBrush, shape)
            )
        }
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
