package com.todayrecord.domain.usecase

import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in Params, Type>(private val coroutineDispatcher: CoroutineDispatcher) {

    operator fun invoke(params: Params): Flow<Result<Type>> {
        return execute(params)
            .catch { e -> emit(Result.Error(e as? Exception ?: Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(params: Params) : Flow<Result<Type>>
}