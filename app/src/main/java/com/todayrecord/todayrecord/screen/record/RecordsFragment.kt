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

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tlRecord.apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_setting -> {
                            recordsViewModel.navigateToSetting()
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
                recordsViewModel.navigateToWriteRecord.collect {
                    findNavController().safeNavigate(RecordsFragmentDirections.actionRecordsFragmentToNavWriteRecord(null))
                }
            }

            launch {
                recordsViewModel.navigateToDetailRecord.collect {
                    findNavController().safeNavigate(RecordsFragmentDirections.actionRecordsFragmentToNavRecordDetail(it))
                }
            }

            launch {
                recordsViewModel.navigateToSetting.collect {
                    findNavController().safeNavigate(RecordsFragmentDirections.actionRecordsFragmentToNavSetting())
                }
            }
        }
    }
}