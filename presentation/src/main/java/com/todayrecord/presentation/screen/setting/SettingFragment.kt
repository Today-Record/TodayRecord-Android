package com.todayrecord.presentation.screen.setting

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.todayrecord.designsystem.component.TodayRecordTopAppBar
import com.todayrecord.designsystem.modifier.singleClickable
import com.todayrecord.designsystem.theme.TodayRecordColor
import com.todayrecord.designsystem.theme.TodayRecordTextStyle
import com.todayrecord.presentation.BuildConfig
import com.todayrecord.presentation.R
import com.todayrecord.presentation.screen.base.ComposableFragment
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : ComposableFragment() {

    private val settingViewModel: SettingViewModel by viewModels()

    @Composable
    override fun RootContent() = SettingScreen(settingViewModel) {
        if (!findNavController().navigateUp()) requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                settingViewModel.navigateToAlarmSetting.collect {
                    findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToAlarmFragment())
                }
            }

            launch {
                settingViewModel.navigateToBinRecords.collect {
                    findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToBinRecordsFragment())
                }
            }

            launch {
                settingViewModel.navigateToRecordPrivacyPolicy.collect {
                    findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToPrivacyPolicyFragment())
                }
            }

            launch {
                settingViewModel.navigateToRecordsClearPopup.collect {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.setting_record_clear))
                        .setMessage(resources.getString(R.string.setting_record_clear_warning_description))
                        .setNegativeButton(resources.getString(R.string.all_cancel)) { dialog, _: Int ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.all_confirm)) { dialog, _: Int ->
                            settingViewModel.clearRecords()
                            dialog.dismiss()
                        }.show()
                }
            }
        }
    }
}

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = viewModel(),
    navigateToBack: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        TodayRecordTopAppBar(
            title = stringResource(id = R.string.setting_title),
            navigationIcon = {
                IconButton(onClick = navigateToBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = "back",
                        tint = Color.Unspecified
                    )
                }
            }
        )
        SettingContent(text = stringResource(id = R.string.setting_alarm), navigateToDetail = viewModel::navigateToAlarmSetting)
        SettingContent(text = stringResource(id = R.string.setting_bin), navigateToDetail = viewModel::navigateToBinRecords)
        SettingContent(text = stringResource(id = R.string.setting_record_privacy_policy), navigateToDetail = viewModel::navigateToRecordPrivacyPolicy)
        SettingContent(text = stringResource(id = R.string.setting_record_clear), navigateToDetail = viewModel::navigateToRecordsClearPopup)
        SettingContent(text = stringResource(id = R.string.setting_app_version), subText = BuildConfig.VERSION_NAME)
    }
}

@Composable
fun SettingContent(
    text: String,
    subText: String? = null,
    navigateToDetail: () -> Unit = {},
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .singleClickable { navigateToDetail() }
                .padding(start = 24.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = TodayRecordColor.color_474a5a,
                style = TodayRecordTextStyle.Body2.asComposeStyle()
            )

            Spacer(modifier = Modifier.weight(1f))

            if (subText == null) {
                Image(
                    painter = painterResource(id = R.drawable.icon_back),
                    contentDescription = "",
                    modifier = Modifier.rotate(180f)
                )
            } else {
                Text(
                    text = subText,
                    color = TodayRecordColor.color_474a5a,
                    style = TodayRecordTextStyle.Body2.asComposeStyle()
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = TodayRecordColor.color_eeeeee)
    }
}