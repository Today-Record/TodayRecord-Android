package com.todayrecord.todayrecord.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ActivityMainBinding
import com.todayrecord.todayrecord.util.Const.KEY_NOTIFY_URI
import com.todayrecord.todayrecord.util.Const.REQUEST_CODE_IN_APP_UPDATE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : DataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    private val mainViewModel: MainViewModel by viewModels()

    private val navController: NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_main_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            mainViewModel.initializeReceiver()
            checkedInAppUpdate()
        }

        navController?.setGraph(R.navigation.nav_main, getNotificationDeeplink(savedInstanceState))
    }

    private fun getNotificationDeeplink(savedInstanceState: Bundle?): Bundle? {
        if (savedInstanceState != null) return null

        val notifyUri = intent.data.also { intent.data = null } ?: return null
        return Bundle().apply { putString(KEY_NOTIFY_URI, notifyUri.toString()) }
    }

    private fun checkedInAppUpdate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    appUpdateManager.requestUpdateFlow()
                        .onEach {
                            when (it) {
                                is AppUpdateResult.Available -> {
                                    val updateVersionCode = it.updateInfo.availableVersionCode()

                                    if (updateVersionCode != mainViewModel.inAppUpdateVersionCode.value) {
                                        mainViewModel.setEnableInAppUpdate(true)
                                    }

                                    if (mainViewModel.enableInAppUpdate.value) {
                                        mainViewModel.setInAppUpdateVersionCode(updateVersionCode)
                                        it.startFlexibleUpdate(this@MainActivity, REQUEST_CODE_IN_APP_UPDATE)
                                    }
                                }
                                is AppUpdateResult.Downloaded -> showAppUpdateSnackBar()
                                is AppUpdateResult.InProgress, is AppUpdateResult.NotAvailable -> return@onEach
                            }
                        }
                        .catch { Timber.e(it, "[checkForImmediateUpdate] message : ${it.message}") }
                        .collect()
                }
            }
        }
    }

    private fun showAppUpdateSnackBar() {
        Snackbar.make(dataBinding.root, resources.getString(R.string.in_app_update_completed), Snackbar.LENGTH_INDEFINITE)
            .setAction(resources.getString(R.string.in_app_update_install_and_restart)) { appUpdateManager.completeUpdate() }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean = navController?.navigateUp() ?: false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IN_APP_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                mainViewModel.setEnableInAppUpdate(false)
            }
        }
    }
}