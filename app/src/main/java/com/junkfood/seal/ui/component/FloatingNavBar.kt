package com.junkfood.seal.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.HapticFeedback.slightHapticFeedback
import com.junkfood.seal.ui.theme.CornerRadius
import com.junkfood.seal.ui.theme.GlassAlpha
import com.junkfood.seal.ui.theme.MotionDuration
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.ui.theme.Spacing

/**
 * A single destination shown by [FloatingNavBar].
 *
 * Kept deliberately dumb (id + vector + label) so the navbar has no dependency on the navigation
 * graph and can be previewed in isolation.
 */
data class FloatingNavDestination(
    val id: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * Total vertical space the floating bar occupies above the navigation bar inset, including its
 * outer margins and breathing room. Host screens use this (directly or via [FloatingNavBarSpacer])
 * as bottom content padding so nothing interactive ends up underneath the bar.
 */
val FloatingNavBarInset = 108.dp

/**
 * Floating, pill-shaped, frosted-glass bottom navigation bar.
 *
 * Design notes / why it is built this way:
 * - It floats above content instead of being a full-width `NavigationBar` so the glass edges stay
 *   visible on all screen sizes and content reads as continuing underneath it.
 * - The "glass" is a translucent surface plus a hairline border rather than a real blur pass, which
 *   keeps scrolling smooth on low-end devices. See [GlassPill].
 * - Selection is expressed with coordinated animations (icon scale, label reveal, colour, padding)
 *   so there is never a frame where the pill background and the active item disagree.
 * - Renders nothing when fewer than two destinations are supplied, which keeps it safe to drop into
 *   any host without a conditional at the call site.
 *
 * @param destinations the items to display, in order.
 * @param currentDestinationId id of the selected destination, or null if none is selected yet.
 * @param onDestinationSelected invoked with the id of the tapped destination.
 * @param showLabels when true the selected item expands to reveal its label.
 */
@Composable
fun FloatingNavBar(
    destinations: List<FloatingNavDestination>,
    currentDestinationId: String?,
    onDestinationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true,
) {
    if (destinations.size < 2) return

    val activeContainer = MaterialTheme.colorScheme.secondaryContainer
    val activeContent = MaterialTheme.colorScheme.onSecondaryContainer
    val inactiveContent = MaterialTheme.colorScheme.onSurfaceVariant

    GlassPill(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Spacing.small),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.forEach { destination ->
                FloatingNavBarItem(
                    destination = destination,
                    selected = destination.id == currentDestinationId,
                    showLabel = showLabels,
                    activeContainer = activeContainer,
                    activeContent = activeContent,
                    inactiveContent = inactiveContent,
                    modifier = Modifier.weight(1f),
                    onClick = { onDestinationSelected(destination.id) },
                )
            }
        }
    }
}

/**
 * One item inside [FloatingNavBar].
 *
 * The selected item gets a softly tinted pill background; unselected ones stay flat so the eye is
 * drawn to one place at a time. `indication = null` is passed deliberately: a rectangular ripple
 * fights the pill shape, and the container/label animation already gives clear press feedback.
 */
@Composable
private fun FloatingNavBarItem(
    destination: FloatingNavDestination,
    selected: Boolean,
    showLabel: Boolean,
    activeContainer: Color,
    activeContent: Color,
    inactiveContent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val view = LocalView.current

    val containerColor by
        animateColorAsState(
            targetValue =
                if (selected) activeContainer.copy(alpha = GlassAlpha.SurfaceStrong) else Color.Transparent,
            animationSpec = tween(durationMillis = MotionDuration.NavIndicator),
            label = "navItemContainer",
        )
    val contentColor by
        animateColorAsState(
            targetValue = if (selected) activeContent else inactiveContent,
            animationSpec = tween(durationMillis = MotionDuration.NavIndicator),
            label = "navItemContent",
        )
    val iconScale by
        animateFloatAsState(
            targetValue = if (selected) 1f else 0.92f,
            animationSpec = tween(durationMillis = MotionDuration.NavIndicator),
            label = "navItemScale",
        )
    val horizontalPadding by
        animateDpAsState(
            targetValue = if (selected && showLabel) Spacing.medium else Spacing.small,
            animationSpec = tween(durationMillis = MotionDuration.NavIndicator),
            label = "navItemPadding",
        )

    Column(
        modifier =
            modifier
                .clip(CornerRadius.pillShape)
                .background(containerColor)
                .selectable(
                    selected = selected,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        view.slightHapticFeedback()
                        onClick()
                    },
                )
                .padding(horizontal = horizontalPadding, vertical = Spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = destination.icon,
            contentDescription = destination.label,
            tint = contentColor,
            modifier = Modifier.size(22.dp).scale(iconScale),
        )
        if (selected && showLabel) {
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = destination.label,
                color = contentColor,
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                maxLines = 1,
            )
        }
    }
}

/**
 * Bottom inset helper so scrollable content is never hidden behind the floating bar.
 *
 * Call this as the final spacer inside the host screen's scroll container.
 */
@Composable
fun FloatingNavBarSpacer(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(FloatingNavBarInset))
}

/**
 * Ready-made destination list matching Snatcher's top-level routes.
 *
 * The ids mirror `Route.HOME`, `Route.DOWNLOADS` and `Route.SETTINGS_PAGE` so the host can pass the
 * current navigation route straight in as `currentDestinationId`.
 */
@Composable
fun rememberDefaultFloatingNavDestinations(): List<FloatingNavDestination> {
    val home = stringResource(R.string.download_queue)
    val downloads = stringResource(R.string.downloads_history)
    val settings = stringResource(R.string.settings)
    return remember(home, downloads, settings) {
        listOf(
            FloatingNavDestination(id = "home", label = home, icon = Icons.Outlined.Download),
            FloatingNavDestination(
                id = "download_history",
                label = downloads,
                icon = Icons.Outlined.Subscriptions,
            ),
            FloatingNavDestination(
                id = "settings_page",
                label = settings,
                icon = Icons.Outlined.Settings,
            ),
        )
    }
}

@Preview
@Composable
private fun FloatingNavBarPreview() {
    SealTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            FloatingNavBar(
                destinations =
                    listOf(
                        FloatingNavDestination("home", "Queue", Icons.Outlined.Download),
                        FloatingNavDestination("download_history", "Downloads", Icons.Outlined.Subscriptions),
                        FloatingNavDestination("settings_page", "Settings", Icons.Outlined.Settings),
                    ),
                currentDestinationId = "download_history",
                onDestinationSelected = {},
            )
        }
    }
}
