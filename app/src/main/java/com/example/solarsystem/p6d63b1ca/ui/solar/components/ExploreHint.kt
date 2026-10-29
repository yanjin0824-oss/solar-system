package com.example.solarsystem.p6d63b1ca.ui.solar.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.solarsystem.p6d63b1ca.R
import com.example.solarsystem.p6d63b1ca.ui.theme.SolarSystemColors
import com.pico.spatial.ui.design.Icon
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text

@Composable
fun ExploreHint(compact: Boolean) {
    val inset = if (compact) 16.dp else 32.dp
    Row(
        modifier = Modifier.padding(start = inset, top = inset),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sun),
            contentDescription = null,
            tint = SolarSystemColors.sunGlow,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "指向行星并点击即可聚焦",
            style = PicoTheme.typography.bodyMedium,
            color = PicoTheme.colorScheme.labelPrimaryLight,
        )
    }
}
