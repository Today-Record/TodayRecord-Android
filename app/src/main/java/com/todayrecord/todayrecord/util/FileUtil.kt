package com.todayrecord.todayrecord.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FileUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

}