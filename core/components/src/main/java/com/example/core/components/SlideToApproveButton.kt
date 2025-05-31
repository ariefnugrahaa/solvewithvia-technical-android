package com.example.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.ButtonContentColor
import com.example.core.ui.theme.DarkTeal
import com.example.core.ui.theme.LightTeal
import com.example.core.ui.theme.TestAriefTheme
import com.example.core.ui.theme.VeryLightTeal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SlideToApproveButton(
    modifier: Modifier = Modifier,
    width: Dp = 160.dp,
    height: Dp = 48.dp,
    onApproved: () -> Unit,
    enabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    var actualWidthPx by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val heightPx = with(density) { height.toPx() }
    val paddingPx = with(density) { 4.dp.toPx() }
    val maxOffset = actualWidthPx - heightPx - paddingPx
    val offsetX = remember { Animatable(0f) }
    var isApproved by remember { mutableStateOf(false) }

    LaunchedEffect(isApproved) {
        if (isApproved) {
            offsetX.animateTo(maxOffset, animationSpec = tween(350))
            delay(500)
            onApproved()
        }
    }

    val alpha = if (enabled) 1f else 0.4f
    val isSwiping = offsetX.value > 0.5f && !isApproved
    val trackColor = when {
        isApproved -> LightTeal
        isSwiping -> LightTeal
        else -> DarkTeal
    }
    val sliderColor = VeryLightTeal

    Surface(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(32.dp))
            .alpha(alpha),
        color = trackColor,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    actualWidthPx = coordinates.size.width.toFloat()
                }
        ) {
            AnimatedVisibility(
                visible = !isApproved,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(height / 2 - 8.dp))
                    Text(
                        text = "Slide to approve",
                        style = MaterialTheme.typography.labelLarge,
                        color = ButtonContentColor
                    )
                }
            }
            AnimatedVisibility(
                visible = isApproved,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = "Approved!",
                    style = MaterialTheme.typography.labelLarge,
                    color = ButtonContentColor
                )
            }
            Box(
                modifier = Modifier
                    .offset { 
                        val currentOffset = offsetX.value.toInt()
                        maxOffset.toInt()
                        val approvedOffset = (maxOffset + 5f).toInt()
                        IntOffset(if (isApproved) approvedOffset else currentOffset, 0)
                    }
                    .size(height)
                    .clip(CircleShape)
                    .background(sliderColor)
                    .then(
                        if (enabled && !isApproved) {
                            Modifier.pointerInput(isApproved) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        scope.launch {
                                            if (offsetX.value < maxOffset * 0.85f) { // Turunkan dari 0.95f ke 0.85f
                                                offsetX.animateTo(0f, animationSpec = tween(350))
                                            } else {
                                                isApproved = true
                                            }
                                        }
                                    }
                                ) { _, dragAmount ->
                                    scope.launch {
                                        val newOffset = (offsetX.value + dragAmount).coerceIn(0f, maxOffset)
                                        offsetX.snapTo(newOffset)
                                    }
                                }
                            }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isApproved) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "Approved",
                        tint = ButtonContentColor,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_feather_chevrons_right),
                        contentDescription = "Slide to approve",
                        tint = ButtonContentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SlideToApproveButtonPreview() {
    TestAriefTheme {
        SlideToApproveButton(
            onApproved = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SlideToApproveButtonDisabledPreview() {
    TestAriefTheme {
        SlideToApproveButton(
            onApproved = {},
            enabled = false
        )
    }
}
