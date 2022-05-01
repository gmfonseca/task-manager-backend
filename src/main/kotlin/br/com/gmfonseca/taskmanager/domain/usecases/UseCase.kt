package br.com.gmfonseca.taskmanager.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UseCase<in Input: Params, out Result> {
    suspend fun execute(params: Input): Result
    operator fun invoke(params: Input): Flow<Result> = flow { execute(params) }
}

interface Params {
    object None: Params
}
