package com.instaleap.challenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instaleap.challenge.data.OperationsCallback
import com.instaleap.challenge.model.Result
import com.instaleap.challenge.model.Results
import com.instaleap.challenge.repository.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: ResultsRepository) : ViewModel() {
    private val _results = MutableLiveData<List<Result>>()
    val results: LiveData<List<Result>> get() = _results
    val isViewLoad: MutableLiveData<Boolean> = MutableLiveData(true)

    fun loadResults() {
        isViewLoad.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository(
                    object : OperationsCallback<Results> {
                        override fun onError(error: String?) {
                            isViewLoad.value = false
                        }

                        override fun onSuccess(data: Results?) {
                            isViewLoad.value = false
                            _results.value = data?.results
                        }
                    }
                )
            }
        }
    }
}