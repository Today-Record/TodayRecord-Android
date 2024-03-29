package com.todayrecord.presentation.screen.write

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.DialogWriteRecordExitBinding
import com.todayrecord.presentation.screen.DataBindingBottomSheetDialogFragment
import com.todayrecord.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteRecordExitBottomSheetFragment : DataBindingBottomSheetDialogFragment<DialogWriteRecordExitBinding>() {

    override val layoutResId: Int = R.layout.dialog_write_record_exit

    private val writeRecordViewModel: WriteRecordViewModel by hiltNavGraphViewModels(R.id.nav_write_record)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = writeRecordViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
    }

    private fun initListener() {
        dataBinding.btnCancel.setOnSingleClickListener {
            dismiss()
        }
    }
}