package com.todayrecord.todayrecord.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.adapter.recorddetail.RecordDetailAdapter
import com.todayrecord.todayrecord.databinding.FragmentRecordDetailBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.Const.KEY_IS_RECORD_UPDATE
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import com.todayrecord.todayrecord.util.widget.RecyclerviewItemDecoration
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

                setOnMenuItemClickListener {
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
                        if (currentDestination?.id == R.id.recordDetailEditBottomSheetDialogFragment) {
                            popBackStack()
                        } else if (currentDestination?.id == R.id.binRecordDetailEditBottomSheetDialogFragment) {
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