package com.instaleap.challenge.repository

import com.instaleap.challenge.data.OperationsCallback
import com.instaleap.challenge.datasource.ResultsDataSource
import com.instaleap.challenge.model.Results

class ResultsRepository(private val resultsDataSource: ResultsDataSource) {
    suspend operator fun invoke(callback: OperationsCallback<Results>) {
        resultsDataSource.results(callback)
    }
}