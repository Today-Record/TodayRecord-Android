package com.todayrecord.todayrecord.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ActivityMainBinding
import com.todayrecord.todayrecord.util.Const.KEY_NOTIFY_URI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModels()

    private val navController: NavController?
        get() = supportFragmentManager.findFragmentById(R.id.nav_main_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            mainViewModel.initializeReceiver()
        }

        navController?.setGraph(R.navigation.nav_main, getNotificationDeeplink(savedInstanceState))
    }

    private fun getNotificationDeeplink(savedInstanceState: Bundle?): Bundle? {
        if (savedInstanceState != null) return null

        val notifyUri = intent.data.also { intent.data = null } ?: return null
        return Bundle().apply { putString(KEY_NOTIFY_URI, notifyUri.toString()) }
    }

    override fun onSupportNavigateUp(): Boolean = navController?.navigateUp() ?: false
}