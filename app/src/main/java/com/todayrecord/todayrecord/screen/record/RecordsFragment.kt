package com.todayrecord.todayrecord.screen.record

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
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
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordsFragment : DataBindingFragment<FragmentRecordsBinding>(R.layout.fragment_records) {

    private val recordsViewModel: RecordsViewModel by viewModels()

    private val recordClickListener = object : RecordClickListener {
        override fun onRecordClick(recordId: String) {
            recordsViewModel.navigateToDetailRecord(recordId)
        }
    }

    private val recordAdapter by lazy { RecordAdapter(recordClickListener) }

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
            adapter = recordAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_record))
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
                    recordAdapter.submitData(it)
                }
            }

            launch {
                recordAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        with(dataBinding) {
                            if (!rvRecord.isVisible && !llRecordEmpty.isVisible) {
                                TransitionManager.beginDelayedTransition(flRecord, TransitionSet().apply {
                                    ordering = TransitionSet.ORDERING_TOGETHER
                                    addTransition(Fade().setStartDelay(300))
                                    duration = 400
                                    interpolator = DecelerateInterpolator()
                                    addTarget(if (recordAdapter.itemCount == 0) llRecordEmpty else rvRecord)
                                })
                            }
                            rvRecord.isVisible = recordAdapter.itemCount > 0
                            llRecordEmpty.isVisible = recordAdapter.itemCount == 0
                        }
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

            launch {
                recordsViewModel.navigateToDeepLink.collect {
                    findNavController().navigate(
                        it,
                        NavOptions.Builder()
                            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .build()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvRecord.adapter = null
        super.onDestroyView()
    }
}