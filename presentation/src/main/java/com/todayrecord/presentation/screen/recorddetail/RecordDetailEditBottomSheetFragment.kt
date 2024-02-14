package com.todayrecord.presentation.screen.recorddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.todayrecord.designsystem.component.TodayRecordButton
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.designsystem.theme.TodayRecordTheme
import com.todayrecord.presentation.R
import com.todayrecord.presentation.screen.base.ComposableBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordDetailEditBottomSheetFragment : ComposableBottomSheetDialogFragment() {

    private val recordDetailViewModel: RecordDetailViewModel by hiltNavGraphViewModels(R.id.nav_record_detail)

    @Composable
    override fun RootContent() = RecordDetailEditBottomSheetScreen(recordDetailViewModel)
}

@Composable
fun RecordDetailEditBottomSheetScreen(
    viewModel: RecordDetailViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = stringResource(id = R.string.record_detail_edit_title),
            color = TodayRecordColor.color_474a5a,
            style = TodayRecordTextStyle.Title.asComposeStyle()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(id = R.string.record_detail_edit_description),
            textAlign = TextAlign.Center,
            color = TodayRecordColor.color_474a5a,
            style = TodayRecordTextStyle.Body1.asComposeStyle()
        )

        Spacer(modifier = Modifier.height(20.dp))

        TodayRecordButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = viewModel::navigateToRecordEdit
        ) {
            Text(
                text = stringResource(id = R.string.all_edit),
                textAlign = TextAlign.Center,
                color = TodayRecordColor.color_ffffff,
                style = TodayRecordTextStyle.Body1.asComposeStyle()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TodayRecordButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = viewModel::navigateToDeletedAndBack,
            color = TodayRecordColor.color_c14d4d
        ) {
            Text(
                text = stringResource(id = R.string.all_delete),
                color = TodayRecordColor.color_ffffff,
                style = TodayRecordTextStyle.Body1.asComposeStyle()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
private fun RecordDetailEditBottomSheetScreenPreview() {
    TodayRecordTheme {
        RecordDetailEditBottomSheetScreen()
    }
}