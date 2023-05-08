package com.todayrecord.todayrecord.data.repository.appinfo

import com.todayrecord.todayrecord.data.datastore.PreferenceStorage
import com.todayrecord.todayrecord.util.type.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : AppInfoRepository {

    override suspend fun setInAppUpdateEnable(isEnable: Boolean) {
        preferenceStorage.prefEnableInAppUpdate(isEnable)
    }

    override fun getInAppUpdateEnable(): Flow<Result<Boolean>> = flow {
        emit(Result.Success(preferenceStorage.enableInAppUpdate.first()))
    }

    override suspend fun setInAppUpdateCode(versionCode: Int) {
        preferenceStorage.prefInAppUpdateVersionCode(versionCode)
    }

    override fun getInAppUpdateCode(): Flow<Result<Int>> = flow {
        emit(Result.Success(preferenceStorage.inAppUpdateVersionCode.first()))
    }
}