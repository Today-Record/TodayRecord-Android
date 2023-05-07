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
import com.todayrecord.todayrecord.adapter.record.RecordsAdapter
import com.todayrecord.todayrecord.databinding.FragmentRecordsBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import com.todayrecord.todayrecord.util.widget.RecordsItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordsFragment : DataBindingFragment<FragmentRecordsBinding>(R.layout.fragment_records) {

    private val recordsViewModel: RecordsViewModel by viewModels()

    private val recordClickListener = object : RecordClickListener {
        override fun onRecordClick(recordId: String) {
            recordsViewModel.navigateToDetailRecord(recordId)
        }
    }
    private val recordsAdapter by lazy { RecordsAdapter(recordClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvRecord.apply {
            recordsAdapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && recordsAdapter.itemCount < 1) {
                    dataBinding.rvRecord.isVisible = false
                    dataBinding.containerEmpty.isVisible = true
                } else {
                    dataBinding.rvRecord.isVisible = true
                    dataBinding.containerEmpty.isVisible = false
                }
            }
            adapter = recordsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecordsItemDecoration(6, 6))
            setHasFixedSize(true)
        }
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
                recordsViewModel.records.collectLatest {
                    recordsAdapter.submitData(it)
                }
            }

            launch {
                recordsViewModel.navigateToWriteRecord.collect {
                    findNavController().safeNavigate(NavMainDirections.actionGlobalNavWriteRecord(null))
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