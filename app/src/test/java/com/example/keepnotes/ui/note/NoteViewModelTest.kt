package com.example.keepnotes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private var noteLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: NoteViewModel
    private var testCurrentNote: Note = Note(id = "0", "Тестируем", text = "Текущая запись")

    @Before
    fun setUp() {
        //every { mockRepository.getNotes() } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

//    @Test
//    fun `should call getNoteById once`() {
//        verify(exactly = 1) { mockRepository.getNoteById() }
//    }

//    @Test
//    fun `should return error`() {
//        var result: Throwable? = null
//        val testData = Throwable("error")
//
//        viewModel.getViewState().observeForever { result = it?.error }
//        noteLiveData.value = NoteResult.Error(testData)
//
//        assertEquals(result, testData)
//    }

    @Test
    fun `fdg dfg`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever {
            result = it?.error }
        noteLiveData.value = NoteResult.Error(testData)

        assertEquals(result, testData)
    }
}