package com.example.solarsystem.p6d63b1ca.ui.solar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.solarsystem.p6d63b1ca.R
import com.example.solarsystem.p6d63b1ca.domain.model.Planet
import com.example.solarsystem.p6d63b1ca.ui.theme.SolarSystemColors
import com.pico.spatial.ui.design.HorizontalDivider
import com.pico.spatial.ui.design.Icon
import com.pico.spatial.ui.design.IconButton
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.design.windows.SpatialPopup
import com.pico.spatial.ui.design.windows.popup.HorizontalPlacement
import com.pico.spatial.ui.design.windows.popup.VerticalPlacement
import com.pico.spatial.ui.design.windows.rememberSpatialPopupPositionProvider

@Composable
fun PlanetFactsCard(
    planet: Planet,
    compact: Boolean,
    onClose: () -> Unit,
) {
    val cardWidth = if (compact) 280.dp else 340.dp
    val cardPadding = if (compact) 16.dp else 24.dp
    val cardGap = if (compact) 12.dp else 16.dp
    val panelWidth = if (compact) 360.dp else 420.dp
    val panelHeight = if (compact) 340.dp else 380.dp

    Box(
        modifier = Modifier.size(panelWidth, panelHeight),
        contentAlignment = Alignment.Center,
    ) {
        SpatialPopup(
            onDismissRequest = onClose,
            cornerRadius = 20.dp,
            defaultMinWidth = cardWidth,
            defaultMinHeight = Dp.Unspecified,
            popupPositionProvider = rememberSpatialPopupPositionProvider(
                horizontalPlacement = HorizontalPlacement.center(),
                verticalPlacement = VerticalPlacement.center(),
            ),
            properties = PopupProperties(
                focusable = true,
                dismissOnClickOutside = false,
            ),
        ) {
            Column(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(cardPadding),
                verticalArrangement = Arrangement.spacedBy(cardGap),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_planet),
                            contentDescription = null,
                            tint = SolarSystemColors.color(planet.colorToken),
                            modifier = Modifier.size(32.dp),
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            // design-style: surface-title fact-title
                            Text(
                                text = planet.name,
                                style = PicoTheme.typography.headlineLarge,
                                color = PicoTheme.colorScheme.labelPrimary,
                            )
                            Text(
                                text = planet.type,
                                style = PicoTheme.typography.bodyMedium,
                                color = PicoTheme.colorScheme.labelSecondary,
                            )
                        }
                    }
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cross),
                            contentDescription = "关闭行星信息",
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = planet.fact,
                        style = PicoTheme.typography.bodyMediumMultiline,
                        color = PicoTheme.colorScheme.labelSecondary,
                    )
                    HorizontalDivider()
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Stat(
                                label = "距太阳距离",
                                value = planet.stats.distanceFromSun,
                                modifier = Modifier.weight(1f),
                            )
                            Stat(
                                label = "自转周期",
                                value = planet.stats.dayLength,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Stat(
                                label = "公转周期",
                                value = planet.stats.yearLength,
                                modifier = Modifier.weight(1f),
                            )
                            Stat(
                                label = "已知卫星",
                                value = planet.stats.knownMoons,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    Text(
                        text = "风格化比例 · 快速事实",
                        style = PicoTheme.typography.labelSmall,
                        color = PicoTheme.colorScheme.labelQuaternary,
                    )
                }
            }
        }
    }
}

@Composable
private fun Stat(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = PicoTheme.typography.bodySmall,
            color = PicoTheme.colorScheme.labelTertiary,
        )
        Text(
            text = value,
            style = PicoTheme.typography.titleSmall,
            color = PicoTheme.colorScheme.labelPrimary,
        )
    }
}
