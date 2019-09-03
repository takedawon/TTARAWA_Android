package com.seoul.ttarawa.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class StateResult<out R> {

    data class Success<out T>(val data: T) : StateResult<T>()
    data class Error(val exception: Throwable) : StateResult<Nothing>()
    object Loading : StateResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }

    companion object {
        fun <T> success(data: T) = Success(data)

        fun error(e: Throwable) = Error(e)

        fun loading() = Loading
    }
}