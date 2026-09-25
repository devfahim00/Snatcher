package com.junkfood.seal.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.junkfood.seal.ui.theme.CornerRadius
import com.junkfood.seal.ui.theme.GlassAlpha
import com.junkfood.seal.ui.theme.MotionDuration
import com.junkfood.seal.ui.theme.SealTheme

/**
 * Animated shimmer brush used by every skeleton placeholder.
 *
 * A single [rememberInfiniteTransition] drives a linear gradient that sweeps horizontally across
 * the placeholder. The alpha range stays narrow so the animation reads as a gentle pulse rather
 * than a flashing block, which keeps long lists comfortable to look at.
 */
@Composable
private fun shimmerBrush(): Brush {
    val base = MaterialTheme.colorScheme.surfaceContainerHighest
    val highlight = MaterialTheme.colorScheme.surface
    val colors =
        remember(base, highlight) {
            listOf(
                base.copy(alpha = GlassAlpha.SkeletonBase),
                highlight.copy(alpha = GlassAlpha.SkeletonHighlight),
                base.copy(alpha = GlassAlpha.SkeletonBase),
            )
        }
    val transition = rememberInfiniteTransition(label = "skeleton")
    val progress by
        transition.animateFloat(
            initialValue = -1f,
            targetValue = 2f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(durationMillis = MotionDuration.Shimmer, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmerProgress",
        )
    return Brush.linearGradient(
        colors = colors,
        start = Offset(x = progress * 320f, y = 0f),
        end = Offset(x = progress * 320f + 240f, y = 240f),
    )
}

/**
 * A single shimmering block. Everything else in this file is built from it, and screens can use it
 * directly for ad-hoc placeholders.
 */
@Composable
fun SkeletonBlock(modifier: Modifier = Modifier, shape: Shape = CornerRadius.smallShape) {
    Box(modifier = modifier.clip(shape).background(shimmerBrush()))
}

/** Convenience placeholder for a line of text. */
@Composable
fun SkeletonText(modifier: Modifier = Modifier, width: Dp? = 120.dp, height: Dp = 14.dp) {
    val sized = if (width == null) modifier.fillMaxWidth() else modifier.width(width)
    SkeletonBlock(modifier = sized.height(height), shape = CornerRadius.smallShape)
}

/**
 * Skeleton matching the geometry of the video card used by the download queue so the loading state
 * does not jump when real content arrives.
 */
@Composable
fun VideoCardSkeleton(modifier: Modifier = Modifier, thumbnailAspectRatio: Float = 16f / 9f) {
    Column(
        modifier = modifier.fillMaxWidth().padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth().aspectRatio(thumbnailAspectRatio),
            shape = CornerRadius.mediumShape,
        )
        SkeletonText(width = 220.dp, height = 16.dp)
        SkeletonText(width = 140.dp, height = 12.dp)
    }
}

/** Skeleton matching the compact grid tile used by the download queue. */
@Composable
fun VideoCardCompactSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth().aspectRatio(1.6f),
            shape = CornerRadius.mediumShape,
        )
        SkeletonText(width = 180.dp, height = 14.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            SkeletonBlock(modifier = Modifier.size(24.dp), shape = CornerRadius.smallShape)
            Spacer(Modifier.width(8.dp))
            SkeletonText(width = 96.dp, height = 12.dp)
        }
    }
}

/** Skeleton for the horizontal list row used when the queue is switched to list mode. */
@Composable
fun ListItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SkeletonBlock(
            modifier = Modifier.width(120.dp).height(72.dp),
            shape = CornerRadius.mediumShape,
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonText(modifier = Modifier, width = null, height = 15.dp)
            SkeletonText(width = 110.dp, height = 12.dp)
        }
    }
}

/** Skeleton for a settings row, matching the height of the real entry. */
@Composable
fun SettingItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SkeletonBlock(modifier = Modifier.size(28.dp), shape = CornerRadius.smallShape)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkeletonText(width = 160.dp, height = 16.dp)
            SkeletonText(width = 220.dp, height = 12.dp)
        }
    }
}

/**
 * Ready-made queue placeholder: a small stack of cards, used while the downloader resolves its first
 * task list so the homepage never flashes an empty state.
 */
@Composable
fun DownloadGridSkeleton(modifier: Modifier = Modifier, itemCount: Int = 4) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(itemCount) { VideoCardCompactSkeleton() }
    }
}

@Preview
@Composable
private fun SkeletonPreview() {
    SealTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            DownloadGridSkeleton(itemCount = 2)
            ListItemSkeleton()
            SettingItemSkeleton()
        }
    }
}
