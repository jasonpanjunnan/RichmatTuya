package com.richmat.mytuya.data.model


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val wxception: Exception) : Result<Nothing>()
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}