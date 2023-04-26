package com.todayrecord.todayrecord.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.adapter.record.RecordDetailAdapter
import com.todayrecord.todayrecord.databinding.FragmentRecordDetailBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.RecyclerviewItemDecoration
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
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

                    findNavController().safeNavigate(RecordDetailFragmentDirections.actionRecordDetailFragmentToNavWritreRecord(it))
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

    override fun onDestroyView() {
        dataBinding.rvRecordDetail.adapter = null
        super.onDestroyView()
    }
}