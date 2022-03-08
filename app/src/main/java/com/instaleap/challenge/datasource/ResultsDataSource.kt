package com.instaleap.challenge.datasource

import com.instaleap.challenge.data.OperationsCallback
import com.instaleap.challenge.model.Results

interface ResultsDataSource {
    suspend fun results(callback: OperationsCallback<Results>)
    fun cancel()
}