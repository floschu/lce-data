package at.florianschuster.data.lce

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Evaluates the suspending [block] and creates [Data.Success] or [Data.Failure] depending on
 * the outcome.
 */
suspend fun <T> dataOf(block: suspend () -> T): Data<T> = try {
    Data.Success(block())
} catch (e: Throwable) {
    Data.Failure(e)
}

/**
 * Creates a [Flow] of [Data] that starts with [Data.Loading] if [loadingTimeInMillis] is > 0L.
 * After the [loadingTimeInMillis] the suspending [block] is evaluated with [dataOf].
 */
fun <T> dataFlowOf(
    loadingTimeInMillis: Long = 0L,
    block: suspend () -> T
): Flow<Data<T>> = flow {
    if (loadingTimeInMillis > 0L) {
        emit(Data.Loading)
        delay(loadingTimeInMillis)
    }
    emit(dataOf(block))
}