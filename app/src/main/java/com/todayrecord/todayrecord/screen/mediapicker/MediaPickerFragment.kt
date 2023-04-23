package com.todayrecord.todayrecord.screen.mediapicker

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.adapter.media.MediaPickerAdapter
import com.todayrecord.todayrecord.adapter.media.SelectedMediaPathAdapter
import com.todayrecord.todayrecord.databinding.FragmentMediaPickerBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.Const.KEY_SELECTED_MEDIA_PATHS
import com.todayrecord.todayrecord.util.Const.REQUEST_CODE_STORAGE_PERMISSION
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

@AndroidEntryPoint
class MediaPickerFragment :
    DataBindingFragment<FragmentMediaPickerBinding>(R.layout.fragment_media_picker),
    EasyPermissions.PermissionCallbacks,
    MediaPickerClickListener {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val mediaPickerViewModel: MediaPickerViewModel by viewModels()

    private val mediaPickerAdapter by lazy { MediaPickerAdapter(this) }
    private val selectedMediaAdapter by lazy { SelectedMediaPathAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            mediaPickerViewModel.initializeMediaPermission(it)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewMdoel = mediaPickerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
        requestStoragePermission()
    }

    private fun initView() {
        with(dataBinding) {
            rvMedia.apply {
                adapter = mediaPickerAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                itemAnimator = null
                setHasFixedSize(true)
            }

            rvSelectedMedia.apply {
                adapter = selectedMediaAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlMediaPicker.apply {
                setNavigationOnClickListener {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_save -> {
                            mediaPickerViewModel.navigateToBack()
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
                mediaPickerViewModel.selectedMediaPaths.collect {
                    selectedMediaAdapter.submitList(it)
                }
            }

            launch {
                mediaPickerViewModel.medias.collectLatest {
                    mediaPickerAdapter.submitData(it)
                }
            }

            launch {
                mediaPickerViewModel.mediaPermission.filterNotNull().filter { !it }.collect {
                    if (!findNavController().navigateUp()) {
                        requireActivity().finish()
                    }
                }
            }

            launch {
                mediaPickerViewModel.navigateToBack.collect {
                    findNavController().run {
                        previousBackStackEntry?.savedStateHandle?.apply {
                            set(KEY_SELECTED_MEDIA_PATHS, it)
                            navigateUp()
                        }
                    }
                }
            }

            launch {
                mediaPickerViewModel.errorMessage.collect {
                    showErrorToast(it)
                }
            }

            launch {
                mediaPickerAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh !is LoadState.NotLoading }
                    .collect { dataBinding.rvMedia.scrollToPosition(0) }
            }
        }
    }

    override fun setSelectedMedia(mediaPath: String) {
        mediaPickerViewModel.setSelectedMedias(mediaPath)
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE_PERMISSION)
    private fun requestStoragePermission() {
        if (EasyPermissions.hasPermissions(requireContext(), *getStoragePermission())) {
            mediaPickerViewModel.initializeMediaPermission(true)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, REQUEST_CODE_STORAGE_PERMISSION, *getStoragePermission())
                    .setRationale(R.string.media_picker_permission_storage)
                    .setPositiveButtonText(R.string.all_confirm)
                    .setNegativeButtonText(R.string.all_cancel)
                    .setTheme(R.style.TodayRecordAlertDialogTheme)
                    .build()
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog
                .Builder(this)
                .setTitle(R.string.media_picker_permission_setting_storage_title)
                .setRationale(R.string.media_picker_permission_setting_storage_description)
                .setNegativeButton(R.string.all_cancel)
                .setPositiveButton(R.string.all_confirm)
                .setThemeResId(R.style.TodayRecordAlertDialogTheme)
                .build()
                .show()
        } else {
            if (perms == getStoragePermission().toList()) {
                mediaPickerViewModel.initializeMediaPermission(false)
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) = Unit

    override fun onDestroyView() {
        dataBinding.apply {
            rvMedia.adapter = null
            rvSelectedMedia.adapter = null
        }
        super.onDestroyView()
    }

    companion object {
        fun getStoragePermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}