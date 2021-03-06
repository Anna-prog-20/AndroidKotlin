package com.example.keepnotes.ui.main

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.keepnotes.model.FireStoreProvider
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        every { mockRepository.getNotes() } returns notesLiveData
        every { mockRepository.deleteNote("1") } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = NoteResult.Error(testData)

        assertEquals(result, testData)
    }

    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note(id = "1"), Note(id = "2"))

        viewModel.getViewState().observeForever { result = it?.data }
        notesLiveData.value = NoteResult.Success(testData)

        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }

    @Test
    fun should_call_deleteNote() {
        viewModel.deleteNote("1")
        verify { mockRepository.deleteNote("1") }
    }
}