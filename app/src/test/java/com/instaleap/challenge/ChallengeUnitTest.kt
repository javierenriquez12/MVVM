package com.instaleap.challenge

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.instaleap.challenge.data.OperationsCallback
import com.instaleap.challenge.datasource.ResultsDataSource
import com.instaleap.challenge.model.Result
import com.instaleap.challenge.model.Results
import com.instaleap.challenge.repository.ResultsRepository
import com.instaleap.challenge.ui.MainViewModel
import org.junit.Test
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.junit.Assert


import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ChallengeUnitTest {
    @Mock
    private lateinit var museumDataSource: ResultsDataSource

    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationsCallback<Results>>

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: ResultsRepository

    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onRenderMuseumsObserver: Observer<List<Result>>

    private lateinit var resultList: Results

    /**
    //https://jeroenmols.com/blog/2019/01/17/livedatajunit5/
    //Method getMainLooper in android.os.Looper not mocked
    java.lang.IllegalStateException: operationCallbackCaptor.capture() must not be null
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(context.applicationContext).thenReturn(context)

        repository = ResultsRepository(museumDataSource)
        viewModel = MainViewModel(repository)

        mockData()
        setupObservers()
    }

    @Test
    fun `retrieve results with ViewModel and Repository returns empty data`() {
        with(viewModel) {
            loadResults()
            isViewLoad.observeForever(isViewLoadingObserver)
            results.observeForever(onRenderMuseumsObserver)
        }
        viewModel.viewModelScope.launch {
            verify(museumDataSource).results((operationCallbackCaptor.capture()))
        }
        Assert.assertNotNull(viewModel.isViewLoad.value)
        Assert.assertTrue(viewModel.results.value?.size == 0)
    }

    @Test
    fun `retrieve results with ViewModel and Repository returns full data`() {
        with(viewModel) {
            loadResults()
            isViewLoad.observeForever(isViewLoadingObserver)
            results.observeForever(onRenderMuseumsObserver)
        }
        viewModel.viewModelScope.launch {
            verify(museumDataSource, times(1)).results(operationCallbackCaptor.capture())
            operationCallbackCaptor.value.onSuccess(resultList)
        }
        Assert.assertNotNull(viewModel.isViewLoad.value)
        Assert.assertTrue(viewModel.results.value?.size == 3)
    }

    private fun setupObservers() {
        isViewLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        onRenderMuseumsObserver = mock(Observer::class.java) as Observer<List<Result>>
    }

    private fun mockData() {
        val mockList: MutableList<Result> = mutableListOf()
        mockList.add(
            Result(
                "Titanic",
                "MOVIE",
            )
        )
        mockList.add(Result("TWD", "Series"))
        mockList.add(Result("BATMAN", "MOVIE"))

        resultList = Results(mockList.toList())
    }
}