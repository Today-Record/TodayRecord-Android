package com.todayrecord.todayrecord.screen.record

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.todayrecord.NavMainDirections
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.adapter.record.RecordAdapter
import com.todayrecord.todayrecord.databinding.FragmentRecordsBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import com.todayrecord.todayrecord.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordFragment : DataBindingFragment<FragmentRecordsBinding>(R.layout.fragment_records) {

    private val recordViewModel: RecordViewModel by viewModels()

    private val recordClickListener = object : RecordClickListener {
        override fun onRecordClick(recordId: String) {
            recordViewModel.navigateToDetailRecord(recordId)
        }
    }
    private val recordAdapter by lazy { RecordAdapter(recordClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvRecord.apply {
            adapter = recordAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(RecyclerviewItemDecoration(6, 6, 0, 0, R.layout.item_record))
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlRecord.apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_setting -> {
                            recordViewModel.navigateToSetting()
                            true
                        }

                        else -> false
                    }
                }
            }
            recordAdapter.apply {
                addLoadStateListener { loadState ->
                    if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && recordAdapter.itemCount < 1) {
                        dataBinding.rvRecord.isVisible = false
                        dataBinding.llRecordEmpty.isVisible = true
                    } else {
                        dataBinding.rvRecord.isVisible = true
                        dataBinding.llRecordEmpty.isVisible = false
                    }
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                recordViewModel.records.collectLatest {
                    recordAdapter.submitData(it)
                }
            }

            launch {
                recordViewModel.navigateToWriteRecord.collect {
                    findNavController().safeNavigate(NavMainDirections.actionGlobalNavWriteRecord(null))
                }
            }

            launch {
                recordViewModel.navigateToDetailRecord.collect {
                    findNavController().safeNavigate(RecordFragmentDirections.actionRecordFragmentToNavRecordDetail(it))
                }
            }

            launch {
                recordViewModel.navigateToSetting.collect {
                    findNavController().safeNavigate(RecordFragmentDirections.actionRecordFragmentToNavSetting())
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvRecord.adapter = null
        super.onDestroyView()
    }
}