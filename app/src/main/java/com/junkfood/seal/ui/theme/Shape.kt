package com.junkfood.seal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

/**
 * App-wide shape scale.
 *
 * The refreshed UI leans on generous, consistent rounding: cards and sheets share the same radius
 * family so a settings card and a download sheet look like they belong to the same system. Values
 * come from [CornerRadius] so the design tokens stay the single source of truth.
 */
val Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(CornerRadius.small / 2),
        small = RoundedCornerShape(CornerRadius.small),
        medium = RoundedCornerShape(CornerRadius.medium),
        large = RoundedCornerShape(CornerRadius.large),
        extraLarge = RoundedCornerShape(CornerRadius.pill),
    )
