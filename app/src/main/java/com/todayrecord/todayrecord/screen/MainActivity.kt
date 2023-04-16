package com.todayrecord.todayrecord.screen

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val navController: NavController
        get() = findNavController(R.id.nav_today_record)

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
}