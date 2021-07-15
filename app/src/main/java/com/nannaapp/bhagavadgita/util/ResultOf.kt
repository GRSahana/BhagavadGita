package com.nannaapp.bhagavadgita.util

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    object Loading: ResultOf<Nothing>()
    sealed class Error(val e: Exception) : ResultOf<Nothing>() {
        class Generic(e: Exception) : Error(e)
        class Error1(e: Exception) : Error(e)
        class Error2(e: Exception) : Error(e)
        class Error3(e: Exception) : Error(e)
    }
}
