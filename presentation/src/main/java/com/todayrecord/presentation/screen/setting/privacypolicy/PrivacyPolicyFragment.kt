package com.todayrecord.presentation.screen.setting.privacypolicy

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.navigation.fragment.findNavController
import com.todayrecord.presentation.R
import com.todayrecord.presentation.databinding.FragmentPrivacyPolicyBinding
import com.todayrecord.presentation.screen.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyFragment : DataBindingFragment<FragmentPrivacyPolicyBinding>(R.layout.fragment_privacy_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        with(dataBinding.wvPrivacyPolicy) {
            settings.apply {
                setSupportZoom(false)
                javaScriptEnabled = true // 자바스크립트 허용
                builtInZoomControls = false
                displayZoomControls = false
                loadWithOverviewMode = true // 메타태그 허용
                useWideViewPort = true
                displayZoomControls = false
                defaultTextEncodingName = "UTF-8"
                domStorageEnabled = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                // Setting Local Storage
                databaseEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            loadUrl(TERMS_URL)
        }
    }

    private fun initListener() {
        dataBinding.tlPrivacyPolicy.setNavigationOnClickListener {
            if (!findNavController().navigateUp()) {
                requireActivity().finish()
            }
        }
    }

    companion object {
        private const val TERMS_URL = "https://quirky-jumper-c99.notion.site/20c506c54bd545e9a868c2a498b52754?pvs=4"
    }
}