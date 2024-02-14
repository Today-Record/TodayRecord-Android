package com.todayrecord.presentation.screen.setting.alarm

import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.FragmentAlarmBinding
import com.todayrecord.presentation.screen.base.DataBindingFragment
import com.todayrecord.presentation.util.Const.ALARM_NOTIFICATION_CHANNEL_ID
import com.todayrecord.presentation.util.Const.ALARM_NOTIFICATION_GROUP_ID
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.listener.DebounceEditTextListener
import com.todayrecord.presentation.util.listener.setOnMenuItemSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlarmFragment : DataBindingFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    @Inject
    lateinit var notificationManager: NotificationManager

    private val alarmViewModel: AlarmViewModel by viewModels()

    private val debounceEditTextListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = alarmViewModel.viewModelScope,
            onDebounceEditTextChange = alarmViewModel::setAlarmMessage
        )
    }

    private val timePickerDialog by lazy {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                alarmViewModel.setAlarmTime(hourOfDay, minute)
            },
            -1,
            -1,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = alarmViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tlAlarm.apply {
                setNavigationOnClickListener {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }

                setOnMenuItemSingleClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            alarmViewModel.saveAlarm()
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                alarmViewModel.enabledTodayRecordAlarmNotification.collect {
                    dataBinding.flPermissionEnable.isVisible = !it
                }
            }

            launch {
                alarmViewModel.navigateToTimePicker.collect {
                    if (!timePickerDialog.isShowing) {
                        timePickerDialog
                            .apply { updateTime(it.hour, it.minute) }
                            .show()
                    }
                }
            }

            launch {
                alarmViewModel.navigateToAlarmSaveAndBack.collect {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }
            }

            launch {
                alarmViewModel.navigateToSystemSetting.collect {
                    if(!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                        }

                        try {
                            startActivity(intent)
                        } catch (exception : Exception) {
                            Timber.e(exception, "[navigateToSystemSetting] message : ${exception.message}")
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                                putExtra(Settings.EXTRA_CHANNEL_ID, ALARM_NOTIFICATION_CHANNEL_ID)
                            }

                            try {
                                startActivity(intent)
                            } catch (exception: Exception) {
                                Timber.e(exception, "[navigateToSystemSetting] message : ${exception.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkEnabledNotification() {
        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
            alarmViewModel.setEnabledTodayRecordAlarmNotification(false)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val isBlockChannelGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    notificationManager.getNotificationChannelGroup(ALARM_NOTIFICATION_GROUP_ID).isBlocked
                } else {
                    false
                }
                val isImportanceNoneChannel = notificationManager.getNotificationChannel(ALARM_NOTIFICATION_CHANNEL_ID).importance == NotificationManager.IMPORTANCE_NONE

                alarmViewModel.setEnabledTodayRecordAlarmNotification(!isBlockChannelGroup && !isImportanceNoneChannel)
            } else {
                alarmViewModel.setEnabledTodayRecordAlarmNotification(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.etComment.addTextChangedListener(debounceEditTextListener)
        checkEnabledNotification()
    }

    override fun onPause() {
        dataBinding.etComment.removeTextChangedListener(debounceEditTextListener)
        super.onPause()
    }
}