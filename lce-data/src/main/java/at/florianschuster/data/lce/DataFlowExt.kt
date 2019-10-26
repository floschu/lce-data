package at.florianschuster.data.lce

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Maps a [Flow] to a [Flow] of [Data]. Throwables are mapped to [Data.Failure]
 * and normal emissions are mapped to [Data.Success].
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.mapAsData(): Flow<Data<T>> =
    map { Data.Success(it) }.catch<Data<T>> { e -> emit(Data.Failure(e)) }

/**
 * Filters [Flow] of [Data] to only emit [Data.Success] type and maps to
 * [Data.Success.value].
 */
fun <T> Flow<Data<T>>.filterSuccessData(): Flow<T> =
    filterIsInstance<Data.Success<T>>().map { it.value }

/**
 * Filters [Flow] of [Data] to only emit [Data.Failure] type and maps to
 * [Data.Failure.error].
 */
fun <T> Flow<Data<T>>.filterFailureData(): Flow<Throwable> =
    filterIsInstance<Data.Failure>().map { it.error }

/**
 * Calls [action] with [Data.Failure.error] on every emission that is a [Data.Failure].
 */
fun <T> Flow<Data<T>>.onEachDataFailure(action: (Throwable) -> Unit): Flow<Data<T>> =
    onEach { data -> if (data is Data.Failure) action(data.error) }

/**
 * Calls [action] with [Data.Success.value] on every emission that is a [Data.Success].
 */
fun <T> Flow<Data<T>>.onEachDataSuccess(action: (T) -> Unit): Flow<Data<T>> =
    onEach { data -> if (data is Data.Success) action(data.value) }