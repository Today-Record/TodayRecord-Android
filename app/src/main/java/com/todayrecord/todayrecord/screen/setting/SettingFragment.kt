package com.todayrecord.todayrecord.screen.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentSettingBinding
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : DataBindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = settingViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tlSetting.apply {
                setNavigationOnClickListener {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }
            }
        }
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