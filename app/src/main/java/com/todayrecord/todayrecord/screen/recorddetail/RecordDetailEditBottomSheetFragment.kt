package com.todayrecord.todayrecord.screen.recorddetail

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.DialogRecordDetailEditBinding
import com.todayrecord.todayrecord.screen.DataBindingBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordDetailEditBottomSheetFragment : DataBindingBottomSheetDialogFragment<DialogRecordDetailEditBinding>() {

    override val layoutResId: Int = R.layout.dialog_record_detail_edit

    private val recordDetailViewModel: RecordDetailViewModel by hiltNavGraphViewModels(R.id.nav_record_detail)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = recordDetailViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}