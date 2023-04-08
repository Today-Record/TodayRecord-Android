package com.todayrecord.todayrecord.screen.record

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentRecordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordsFragment: DataBindingFragment<FragmentRecordsBinding>(R.layout.fragment_records) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}