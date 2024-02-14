package com.todayrecord.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.todayrecord.designsystem.R
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.designsystem.theme.TodayRecordTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayRecordTopAppBar(
    modifier: Modifier = Modifier,
    thickness: Dp = 0.5.dp,
    title: String = "",
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    Column {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    style = TodayRecordTextStyle.Body2.asComposeStyle(),
                    color = TodayRecordColor.color_474a5a,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = WindowInsets(top = 0.dp),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = TodayRecordColor.color_ffffff,
                navigationIconContentColor = TodayRecordColor.color_474a5a,
                titleContentColor = TodayRecordColor.color_474a5a
            )
        )

        if (thickness != 0.dp) {
            HorizontalDivider(thickness = thickness, color = TodayRecordColor.color_474a5a)
        }
    }
}

@Preview
@Composable
private fun TodayRecordTopAppBarPreview() {
    TodayRecordTheme {
        TodayRecordTopAppBar(
            title = "기록들",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = "back",
                        tint = Color.Unspecified
                    )
                }
            },
            actions = {
                TextButton(onClick = { }) {
                    Text(
                        text = "설정",
                        color = TodayRecordColor.color_474a5a,
                        style = TodayRecordTextStyle.Body2.asComposeStyle()
                    )
                }
            }
        )
    }
}