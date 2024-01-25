package com.todayrecord.presentation.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.presentation.R
import com.todayrecord.presentation.adapter.recorddetail.RecordDetailAdapter
import com.todayrecord.presentation.databinding.FragmentRecordDetailBinding
import com.todayrecord.presentation.screen.DataBindingFragment
import com.todayrecord.presentation.util.Const.KEY_IS_RECORD_UPDATE
import com.todayrecord.presentation.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.presentation.util.listener.setOnMenuItemSingleClickListener
import com.todayrecord.presentation.util.safeNavigate
import com.todayrecord.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordDetailFragment : DataBindingFragment<FragmentRecordDetailBinding>(R.layout.fragment_record_detail) {

    private val recordDetailViewModel: RecordDetailViewModel by hiltNavGraphViewModels(R.id.nav_record_detail)

    private val recordDetailAdapter by lazy { RecordDetailAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordDetailViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
        initNavBackstackObserve()
    }

    private fun initView() {
        with(dataBinding) {
            rvRecordDetail.apply {
                adapter = recordDetailAdapter
                layoutManager = LinearLayoutManager(requireContext())
                overScrollMode = View.OVER_SCROLL_NEVER
                addItemDecoration(RecyclerviewItemDecoration(0, 8, 0, 0, R.layout.item_record_detail_image, R.layout.item_record_detail_text))
                setHasFixedSize(true)
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlRecordDetail.apply {
                setNavigationOnClickListener {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }

                setOnMenuItemSingleClickListener {
                    when (it.itemId) {
                        R.id.menu_edit -> {
                            recordDetailViewModel.navigateToRecordDetailEditDialog()
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
                recordDetailViewModel.recordContentItem.collect {
                    recordDetailAdapter.submitList(it)
                }
            }

            launch {
                recordDetailViewModel.navigateToEditDialog.collect {
                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToRecordDetailEditBottomSheetDialogFragment())
                }
            }

            launch {
                recordDetailViewModel.navigateToBinEditDialog.collect {
                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToBinRecordDetailEditBottomSheetDialogFragment())
                }
            }

            launch {
                recordDetailViewModel.navigateToRecordEdit.collect {
                    if (findNavController().currentDestination?.id == R.id.recordDetailEditBottomSheetDialogFragment) {
                        findNavController().popBackStack()
                    }

                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToNavWriteRecord(it))
                }
            }

            launch {
                recordDetailViewModel.navigateToBack.collect {
                    with(findNavController()) {
                        if (currentDestination?.id == R.id.recordDetailEditBottomSheetDialogFragment
                            || currentDestination?.id == R.id.binRecordDetailEditBottomSheetDialogFragment
                        ) {
                            popBackStack()
                        }

                        if (!navigateUp()) {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun initNavBackstackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recordDetailFragment)
        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_IS_RECORD_UPDATE)) return@LifecycleEventObserver
                val isUpdated: Boolean = navBackStackEntry.savedStateHandle[KEY_IS_RECORD_UPDATE] ?: false

                if (isUpdated) recordDetailViewModel.onRefresh()
                navBackStackEntry.savedStateHandle.remove<Boolean>(KEY_IS_RECORD_UPDATE)
            }
        }

        navBackStackEntry.lifecycle.addObserver(resultObserver)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(resultObserver)
            }
        })
    }

    override fun onDestroyView() {
        dataBinding.rvRecordDetail.adapter = null
        super.onDestroyView()
    }
}