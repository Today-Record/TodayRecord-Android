package com.todayrecord.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.designsystem.theme.TodayRecordTheme

private const val ButtonPressedScale = 0.95f
private const val ButtonPressedAlpha = 0.75f
private val ButtonPressedAnimation = tween<Float>()

@Composable
fun TodayRecordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    color: Color = TodayRecordColor.color_474a5a,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) ButtonPressedScale else 1f,
        animationSpec = ButtonPressedAnimation,
        label = "ScaleAnimation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) ButtonPressedAlpha else 1f,
        animationSpec = ButtonPressedAnimation,
        label = "AlphaAnimation"
    )

    Button(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
            .height(height),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Preview
@Composable
private fun TodayRecordButtonPreview() {
    TodayRecordTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TodayRecordButton(
                modifier = Modifier.fillMaxWidth(),
                color = TodayRecordColor.color_474a5a,
                onClick = {}
            ) {
                Text(
                    text = "저장하기",
                    style = TodayRecordTextStyle.Body1.asComposeStyle()
                )
            }

            TodayRecordButton(
                modifier = Modifier.fillMaxWidth(),
                color = TodayRecordColor.color_c14d4d,
                onClick = {}
            ) {
                Text(
                    text = "삭제하기",
                    style = TodayRecordTextStyle.Body1.asComposeStyle()
                )
            }
        }
    }
}