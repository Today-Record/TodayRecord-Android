package com.todayrecord.todayrecord.screen.write

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.adapter.write.WriteRecordImageAdapter
import com.todayrecord.todayrecord.databinding.FragmentWriteRecordBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.Const.KEY_IS_RECORD_UPDATE
import com.todayrecord.todayrecord.util.Const.KEY_SELECTED_MEDIA_PATHS
import com.todayrecord.todayrecord.util.hideKeyboard
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.listener.DebounceEditTextListener
import com.todayrecord.todayrecord.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

@AndroidEntryPoint
class WriteRecordFragment : DataBindingFragment<FragmentWriteRecordBinding>(R.layout.fragment_write_record) {

    private val writeRecordViewModel: WriteRecordViewModel by hiltNavGraphViewModels(R.id.nav_write_record)

    private val writeRecordClickListener = object : WriteRecordClickListener {
        override fun onRecordImageDeletedListener(image: String) {
            writeRecordViewModel.deleteRecordImages(image)
        }
    }

    private val writeRecordImageAdapter by lazy { WriteRecordImageAdapter(writeRecordClickListener) }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (writeRecordViewModel.isRecordSaveEnable.value) {
                dataBinding.root.hideKeyboard()
                findNavController().safeNavigate(WriteRecordFragmentDirections.actionWriteRecordFragmentToWriteRecordExitBottomSheetDialogFragment())
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private val debounceEditTextListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = writeRecordViewModel.viewModelScope,
            onDebounceEditTextChange = writeRecordViewModel::existRecordText
        )
    }

    private val datePickerDialog by lazy {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                writeRecordViewModel.updateRecordDate(year, month.plus(1), dayOfMonth)
            },
            1900,
            1,
            1
        ).apply {
            this.datePicker.maxDate = Instant.now().toEpochMilli()
        }
    }

    private val timePickerDialog by lazy {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                writeRecordViewModel.updateRecordTime(hourOfDay, minute)
            },
            -1,
            -1,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = writeRecordViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
        initNavBackstackObserve()
    }

    private fun initView() {
        dataBinding.rvSelectedMedia.apply {
            adapter = writeRecordImageAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        with(dataBinding) {
            tlWriteRecord.apply {
                setNavigationOnClickListener {
                    hideKeyboard()
                    onBackPressedCallback.handleOnBackPressed()
                }

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            if (!writeRecordViewModel.isRecordSaveEnable.value) {
                                showErrorToast(R.string.write_record_text_length_error)
                            } else {
                                lifecycleScope.launch {
                                    hideKeyboard()
                                    delay(200)
                                    writeRecordViewModel.saveRecord()
                                }
                            }
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
                writeRecordViewModel.recordImages.collect {
                    writeRecordImageAdapter.submitList(it)
                }
            }

            launch {
                writeRecordViewModel.navigateToDatePicker.collect {
                    if (!datePickerDialog.isShowing) {
                        datePickerDialog
                            .apply { updateDate(it.year, it.monthValue.minus(1), it.dayOfMonth) }
                            .show()
                    }
                }
            }

            launch {
                writeRecordViewModel.navigateToTimePicker.collect {
                    if (!timePickerDialog.isShowing) {
                        timePickerDialog
                            .apply { updateTime(it.hour, it.minute) }
                            .show()
                    }
                }
            }

            launch {
                writeRecordViewModel.navigateToMediaPicker.collect {
                    if (it == 0) {
                        showErrorToast(requireContext().getString(R.string.write_record_image_count_error))
                    } else {
                        findNavController().safeNavigate(WriteRecordFragmentDirections.actionWriteRecordFragmentToMediaPickerFragment(it))
                    }
                }
            }

            launch {
                writeRecordViewModel.navigateToBack.collect {
                    findNavController().run {
                        val isWriteRecordExit = currentDestination?.id == R.id.writeRecordExitBottomSheetDialogFragment
                        if (isWriteRecordExit) popBackStack()

                        previousBackStackEntry?.savedStateHandle?.apply {
                            set(KEY_IS_RECORD_UPDATE, !isWriteRecordExit)
                            navigateUp()
                        }
                    }
                }
            }

            launch {
                writeRecordViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                writeRecordViewModel.errorMessage.collect {
                    showErrorToast(it)
                }
            }
        }
    }

    private fun initNavBackstackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.writeRecordFragment)
        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_SELECTED_MEDIA_PATHS)) return@LifecycleEventObserver
                val selectedImages: List<String> = navBackStackEntry.savedStateHandle[KEY_SELECTED_MEDIA_PATHS] ?: emptyList()
                writeRecordViewModel.setRecordImages(selectedImages)

                navBackStackEntry.savedStateHandle.remove<List<String>?>(KEY_SELECTED_MEDIA_PATHS)
            }
        }

        navBackStackEntry.lifecycle.addObserver(resultObserver)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(resultObserver)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        dataBinding.etWriteRecord.addTextChangedListener(debounceEditTextListener)
    }

    override fun onPause() {
        dataBinding.etWriteRecord.removeTextChangedListener(debounceEditTextListener)
        super.onPause()
    }

    override fun onDestroyView() {
        dataBinding.rvSelectedMedia.adapter = null
        super.onDestroyView()
    }
}