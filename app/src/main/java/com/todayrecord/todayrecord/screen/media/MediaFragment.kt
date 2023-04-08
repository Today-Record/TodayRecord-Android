package com.todayrecord.todayrecord.screen.media

import android.os.Bundle
import android.view.View
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentMediaBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : DataBindingFragment<FragmentMediaBinding>(R.layout.fragment_media) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}