package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.submission_1_intermediet.adapter.ListStoriesAdapter
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: SharedPreferences

    @Mock
    private lateinit var repo : UserRepositoryImpl

    //viewModel
    @Test
    fun`testing setDeleteSesiandToken delete token and sesi`() = runTest {

        val viewModel = MainViewModel(repo, pref)

        viewModel.setDeleteSesiandToken()
        Mockito.verify(repo).deleteTokenandSesi()
    }

    @Test
    fun`testing loading value are changes after using function`() = runTest {

        val viewModel = MainViewModel(repo, pref)

        viewModel.changeLoadingState(true)
        Assert.assertEquals(viewModel.isLoading.value, true)
        Assert.assertNotNull(viewModel.isLoading.value)

        viewModel.changeLoadingState(false)
        Assert.assertEquals(viewModel.isLoading.value, false)
        Assert.assertNotNull(viewModel.isLoading.value)
    }

    @Test
    fun`when getStories paging data viewmodel not Null and has value`() = runTest {

        val viewModel = MainViewModel(repo, pref)
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        val data = PagingTesting.snapshot(DataDummy.generateDummyStoryPagingResponse())
        val expected: Flow<PagingData<ListStoryItem>> = flow {
            emit(data)
        }

        `when`(repo.getStories()).thenReturn(expected)

        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.getData().collect {
                differ.submitData(it)
            }
        }

        Mockito.verify(repo).getStories()
        Assert.assertEquals(differ.snapshot(), DataDummy.generateDummyStory().listStory)
        Assert.assertNotNull(differ.snapshot())
        collectJob.cancel()
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}