package com.todayrecord.todayrecord.screen.setting.alarm

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentAlarmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment: DataBindingFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}