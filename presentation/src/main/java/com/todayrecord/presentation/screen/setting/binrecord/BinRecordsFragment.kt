package com.todayrecord.presentation.screen.setting.binrecord

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.todayrecord.presentation.R
import com.todayrecord.presentation.adapter.record.RecordAdapter
import com.todayrecord.presentation.databinding.FragmentBinRecordsBinding
import com.todayrecord.presentation.screen.DataBindingFragment
import com.todayrecord.presentation.screen.record.RecordClickListener
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.safeNavigate
import com.todayrecord.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BinRecordsFragment : DataBindingFragment<FragmentBinRecordsBinding>(R.layout.fragment_bin_records) {

    private val binRecordsViewModel: BinRecordsViewModel by viewModels()

    private val binRecordClickListener = object : RecordClickListener {
        override fun onRecordClick(recordId: String) {
            binRecordsViewModel.navigateToDetailRecord(recordId)
        }
    }

    private val binRecordAdapter by lazy { RecordAdapter(binRecordClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = binRecordsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvBinRecord.apply {
            adapter = binRecordAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_record))
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        dataBinding.tlBinRecord.setNavigationOnClickListener {
            if (!findNavController().navigateUp()) {
                requireActivity().finish()
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                binRecordsViewModel.binRecords.collectLatest {
                    binRecordAdapter.submitData(it)
                }
            }

            launch {
                binRecordAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        with(dataBinding) {
                            if (!rvBinRecord.isVisible && !llBinRecordEmpty.isVisible) {
                                TransitionManager.beginDelayedTransition(flBinRecord, TransitionSet().apply {
                                    ordering = TransitionSet.ORDERING_TOGETHER
                                    addTransition(Fade().setStartDelay(300))
                                    duration = 400
                                    interpolator = DecelerateInterpolator()
                                    addTarget(if (binRecordAdapter.itemCount == 0) llBinRecordEmpty else rvBinRecord)
                                })
                            }
                            rvBinRecord.isVisible = binRecordAdapter.itemCount > 0
                            llBinRecordEmpty.isVisible = binRecordAdapter.itemCount == 0
                            fabBinClear.isVisible = binRecordAdapter.itemCount > 0
                        }
                    }
            }

            launch {
                binRecordsViewModel.navigateToBinClearPopup.collect {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.bin_record_clear_popup_title))
                        .setMessage(resources.getString(R.string.bin_record_clear_popup_description))
                        .setNegativeButton(resources.getString(R.string.all_cancel)) { dialog, _: Int ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.all_confirm)) { dialog, _: Int ->
                            binRecordsViewModel.clearBinRecords()
                            dialog.dismiss()
                        }.show()
                }
            }

            launch {
                binRecordsViewModel.navigateToDetailRecord.collect {
                    findNavController().safeNavigate(BinRecordsFragmentDirections.actionBinRecordsFragmentToNavRecordDetail(it))
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvBinRecord.adapter = null
        super.onDestroyView()
    }
}