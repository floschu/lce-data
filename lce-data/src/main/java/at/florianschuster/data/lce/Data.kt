package at.florianschuster.data.lce

/**
 * Sealed class that represents an asynchronous load of a data [value].
 */
sealed class Data<out T> {
    /**
     * Returns the value [value] if it is available, otherwise null.
     */
    open operator fun invoke(): T? = null

    /**
     * Represents the initial state of the data.
     */
    object Uninitialized : Data<Nothing>()

    /**
     * Represents the loading state of the data.
     */
    object Loading : Data<Nothing>()

    /**
     * Represents the failed state of the data containing the [error] cause.
     */
    data class Failure(val error: Throwable) : Data<Nothing>()

    /**
     * Represents the successful state of the data containing the [value].
     */
    data class Success<out T>(val value: T) : Data<T>() {
        override operator fun invoke(): T = value
    }

    val uninitialized: Boolean get() = this is Uninitialized
    val loading: Boolean get() = this is Loading
    val failed: Boolean get() = this is Failure
    val successful: Boolean get() = this is Success
    val complete: Boolean get() = this is Error || this is Success

    override fun equals(other: Any?): Boolean = when {
        this is Uninitialized && other is Uninitialized -> true
        this is Loading && other is Loading -> true
        this is Failure && other is Failure && this.error == other.error -> true
        this is Success && other is Success<*> && this.value == other.value -> true
        else -> false
    }

    companion object {

        /**
         * Invoke this to create either a [Data.Success] or [Data.Failure] from the [block].
         */
        inline operator fun <T> invoke(crossinline block: () -> T): Data<T> =
            try {
                Success(block())
            } catch (e: Throwable) {
                Failure(e)
            }
    }
}