package com.instaleap.challenge.data

interface OperationsCallback<T> {
    fun onSuccess(data: T?)
    fun onError(error: String?)
}