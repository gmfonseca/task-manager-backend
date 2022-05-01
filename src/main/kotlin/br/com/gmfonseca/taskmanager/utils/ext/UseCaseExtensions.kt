package br.com.gmfonseca.taskmanager.utils.ext

import br.com.gmfonseca.taskmanager.domain.usecases.Params.None
import br.com.gmfonseca.taskmanager.domain.usecases.UseCase
import kotlinx.coroutines.flow.Flow

operator fun <Result> UseCase<None, Result>.invoke(): Flow<Result> = invoke(None)
suspend fun <Result> UseCase<None, Result>.execute(): Result = execute(None)
