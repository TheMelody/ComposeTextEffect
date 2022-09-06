package com.melody.text.effect.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.rippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(
        indication = rememberRipple(),
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}