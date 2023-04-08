package com.todayrecord.todayrecord.screen.setting.binrecord

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentBinRecordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BinRecordsFragment : DataBindingFragment<FragmentBinRecordsBinding>(R.layout.fragment_bin_records) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}