package com.ayukrisna.dicodingstory.view.ui.component

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ayukrisna.dicodingstory.R

@Composable
fun AnimatedMovingImageVertical(
    painter: Painter,
    description: String,
    initialValue : Float = -20f,
    targetValue: Float = 20f,
    durationMillis: Int = 3000,
    easing: Easing = LinearEasing
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AnimatedMovingImage")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        ), label = "AnimatedMovingImage"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.wrapContentSize()
    ) {
        Image(
            painter = painter,
            contentDescription = description,
            modifier = Modifier
                .size(250.dp)
                .offset(x = offsetX.dp)
        )
    }
}

@Preview
@Composable
fun AnimationPreview() {
    AnimatedMovingImageVertical(
        painter = painterResource(id = R.drawable.image_signup),
        description = "Testing Preview"
    )
}