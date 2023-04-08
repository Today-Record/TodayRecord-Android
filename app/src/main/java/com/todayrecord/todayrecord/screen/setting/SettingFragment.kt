package com.todayrecord.todayrecord.screen.setting

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: DataBindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}