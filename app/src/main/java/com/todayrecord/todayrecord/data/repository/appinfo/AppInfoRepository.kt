package com.todayrecord.todayrecord.data.repository.appinfo

import com.todayrecord.todayrecord.util.type.Result
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {

    suspend fun setInAppUpdateEnable(isEnable: Boolean)

    fun getInAppUpdateEnable() : Flow<Result<Boolean>>

    suspend fun setInAppUpdateCode(versionCode: Int)

    fun getInAppUpdateCode() : Flow<Result<Int>>
}