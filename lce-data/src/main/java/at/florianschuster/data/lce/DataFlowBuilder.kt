package at.florianschuster.data.lce

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Evaluates the suspending [block] and creates [Data.Success] or [Data.Failure] depending
 * on the outcome.
 */
suspend inline fun <T> dataOf(
    crossinline block: suspend () -> T
): Data<T> = try {
    Data.Success(block())
} catch (e: Throwable) {
    Data.Failure(e)
}

/**
 * Creates a [Flow] of [Data] that starts with [Data.Loading] and then emits the [block]
 * that is evaluated with [dataOf].
 */
inline fun <T> dataFlowOf(
    crossinline block: suspend () -> T
): Flow<Data<T>> = flow {
    emit(Data.Loading)
    emit(dataOf(block))
}