package com.todayrecord.todayrecord.screen.record

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentRecordsBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordsFragment : DataBindingFragment<FragmentRecordsBinding>(R.layout.fragment_records) {

    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                recordsViewModel.navigateToWriteRecord.collect {
                    findNavController().safeNavigate(RecordsFragmentDirections.actionRecordsFragmentToNavWriteRecord(null))
                }
            }
        }
    }
}