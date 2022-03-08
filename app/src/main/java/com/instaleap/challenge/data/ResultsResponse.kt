package com.instaleap.challenge.data

import com.google.gson.annotations.SerializedName

data class ResultsResponse(
    @SerializedName("results")
    var results: ArrayList<ResultResponse> = arrayListOf()
)

data class ResultResponse(
    @SerializedName("name")
    var name: String,
    @SerializedName("type")
    var type: String
)