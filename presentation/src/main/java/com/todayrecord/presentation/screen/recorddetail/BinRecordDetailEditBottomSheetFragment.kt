package com.todayrecord.presentation.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.DialogBinRecordDetailEditBinding
import com.todayrecord.presentation.screen.DataBindingBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BinRecordDetailEditBottomSheetFragment : DataBindingBottomSheetDialogFragment<DialogBinRecordDetailEditBinding>() {

    override val layoutResId: Int = R.layout.dialog_bin_record_detail_edit

    private val recordDetailViewModel: RecordDetailViewModel by hiltNavGraphViewModels(R.id.nav_record_detail)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordDetailViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}