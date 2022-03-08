package com.instaleap.challenge.di

import com.instaleap.challenge.data.ApiClient
import com.instaleap.challenge.data.ResultsRemoteDataSource
import com.instaleap.challenge.datasource.ResultsDataSource
import com.instaleap.challenge.repository.ResultsRepository

class Injection {
    private var resultsDataSource: ResultsDataSource? = null
    private var resultsRepository: ResultsRepository? = null

    private fun createResultsDataSource(): ResultsDataSource {
        val dataSource = ResultsRemoteDataSource(ApiClient)
        resultsDataSource = dataSource
        return dataSource
    }

    private fun createResultsRepository(): ResultsRepository {
        val repository = ResultsRepository(provideDataSource())
        resultsRepository = repository
        return repository
    }


    private fun provideDataSource() = resultsDataSource ?: createResultsDataSource()
    fun providerResultRepository() = resultsRepository ?: createResultsRepository()

    fun destroy() {
        resultsDataSource = null
        resultsRepository = null
    }
}