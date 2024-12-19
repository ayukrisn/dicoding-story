package com.ayukrisna.dicodingstory.ui

import com.ayukrisna.dicodingstory.MainDispatcherRule
import com.ayukrisna.dicodingstory.domain.usecase.ListStoryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.ayukrisna.dicodingstory.DataDummy
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.mockito.Mockito
import com.ayukrisna.dicodingstory.view.ui.screen.story.liststory.ListStoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var listStoryUseCase: ListStoryUseCase

    private lateinit var viewModel: ListStoryViewModel

    @Test
    fun `when Get Story should not null and return data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableStateFlow<PagingData<ListStoryItem>>(PagingData.empty())
        expectedStory.value = data

        Mockito.`when`(listStoryUseCase.loadStory()).thenReturn(expectedStory)
        viewModel = ListStoryViewModel(listStoryUseCase)

        launch {
            viewModel.loadStory()
        }

        advanceUntilIdle()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<ListStoryItem>(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        val actualData = viewModel.stories
        differ.submitData(actualData.value)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().items.size)
        Assert.assertEquals(dummyStory[0], differ.snapshot().items[0])
    }

    @Test
    fun `when No Story Data Should Return Zero Items`() = runTest {
        val expectedStory = MutableStateFlow<PagingData<ListStoryItem>>(PagingData.empty())

        Mockito.`when`(listStoryUseCase.loadStory()).thenReturn(expectedStory)
        viewModel = ListStoryViewModel(listStoryUseCase)

        launch {
            viewModel.loadStory()
        }

        advanceUntilIdle()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback<ListStoryItem>(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        val actualData = viewModel.stories
        differ.submitData(actualData.value)

        Assert.assertTrue(differ.snapshot().items.isEmpty())
    }
}

class StoryPagingSource : PagingSource<Int, Flow<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class TestDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}