package com.todayrecord.todayrecord.screen.mediapicker

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentMediaPickerBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaPickerFragment : DataBindingFragment<FragmentMediaPickerBinding>(R.layout.fragment_media_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}