package com.todayrecord.todayrecord.screen.intro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.*
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.FragmentIntroBinding
import com.todayrecord.todayrecord.screen.DataBindingFragment
import com.todayrecord.todayrecord.util.launchAndRepeatWithViewLifecycle
import com.todayrecord.todayrecord.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroFragment : DataBindingFragment<FragmentIntroBinding>(R.layout.fragment_intro) {

    private val introViewModel: IntroViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            introViewModel.navigateToRecords()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        hideSystemUi()
    }

    private fun initListener() {
        startSlideAnimation {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPostNotificationPermission()
            } else {
                introViewModel.navigateToRecords()
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                introViewModel.navigateToRecords.collect {
                    findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToRecordsFragment())
                }
            }

            launch {
                introViewModel.navigateToDeepLink.collect {
                    findNavController().navigate(
                        it,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.introFragment, inclusive = true, saveState = false)
                            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .build()
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPostNotificationPermission() {
        val isPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
        val isShowPermissionRequest = shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        when {
            isPermissionGranted == PackageManager.PERMISSION_GRANTED || isShowPermissionRequest -> introViewModel.navigateToRecords()
            else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startSlideAnimation(ended: () -> Unit) {
        with(dataBinding) {
            root.post {
                TransitionManager.beginDelayedTransition(flIntro, TransitionSet().apply {
                    ordering = TransitionSet.ORDERING_TOGETHER
                    addTransition(Fade().setStartDelay(300))
                    addTransition(Slide())
                    duration = 400
                    interpolator = DecelerateInterpolator()
                    addTarget(llIntro)
                }.addListener(
                    object : TransitionListenerAdapter() {
                        override fun onTransitionEnd(transition: Transition) {
                            super.onTransitionEnd(transition)
                            ended()
                        }
                    }
                ))
                llIntro.isVisible = true
            }
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, dataBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, dataBinding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onDestroyView() {
        showSystemUi()
        super.onDestroyView()
    }
}