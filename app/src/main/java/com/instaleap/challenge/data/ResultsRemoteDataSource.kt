package com.instaleap.challenge.data

import com.instaleap.challenge.datasource.ResultsDataSource
import com.instaleap.challenge.model.Result
import com.instaleap.challenge.model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsRemoteDataSource(apiClient: ApiClient) : ResultsDataSource {

    private var call: Call<ResultsResponse>? = null
    private val service = apiClient.build()

    override suspend fun results(callback: OperationsCallback<Results>) {

        call = service.results()
        call?.enqueue(object : Callback<ResultsResponse> {
            override fun onFailure(call: Call<ResultsResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<ResultsResponse>,
                response: Response<ResultsResponse>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        val results =
                            Results(
                                it.results.map {
                                    Result(it.name, it.type)
                                } as ArrayList<Result>
                            )
                        callback.onSuccess(results)
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.let {
            it.cancel()
        }
    }
}