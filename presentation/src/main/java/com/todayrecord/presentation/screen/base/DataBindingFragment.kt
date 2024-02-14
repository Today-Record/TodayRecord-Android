package com.todayrecord.presentation.screen.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.todayrecord.presentation.databinding.DialogLoadingBinding

abstract class DataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    private var _dataBinding: T? = null

    protected val dataBinding: T
        get() = _dataBinding!!

    private val loadingDialog: AppCompatDialog by lazy {
        DialogLoadingBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            .run {
                AppCompatDialog(requireContext())
                    .apply {
                        setCancelable(false)
                        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        setContentView(this@run.root)
                    }
            }
    }

    fun showLoadingDialog() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    fun hideLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showErrorToast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return dataBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingDialog()
        _dataBinding = null
    }
}