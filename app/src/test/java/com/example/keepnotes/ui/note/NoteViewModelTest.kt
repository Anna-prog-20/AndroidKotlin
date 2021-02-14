package com.example.keepnotes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.main.MainViewModel
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<NoteResult>()
    private val currentNote = Note("1", "topic", "text")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setUp() {
        every { mockRepository.saveNote(currentNote) } returns notesLiveData
        every { mockRepository.getNoteById(currentNote.id) } returns notesLiveData
        every { mockRepository.deleteNote(currentNote.id) } returns notesLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should save changes note`() {
        viewModel.saveChanges(currentNote)
        viewModel.onCleared()
        verify { mockRepository.saveNote(currentNote) }
    }

    @Test
    fun `loadNote should return NoteViewState`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, currentNote)

        viewModel.getViewState().observeForever {
            result = it.data
        }
        viewModel.loadNote(currentNote.id)
        notesLiveData.value = NoteResult.Success(currentNote)

        assertEquals(result, testData)
    }

    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever {
            result = it.error
        }
        viewModel.loadNote(currentNote.id)
        notesLiveData.value = NoteResult.Error(testData)

        assertEquals(result, testData)
    }

    @Test
    fun `deleteNote should return NoteViewState with isDeleted = true`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)

        viewModel.getViewState().observeForever {
            result = it.data
        }
        viewModel.saveChanges(currentNote)
        viewModel.deleteNote()
        notesLiveData.value = NoteResult.Success(null)

        assertEquals(result, testData)
    }

    @Test
    fun `deleteNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever {
            result = it.error
        }
        viewModel.saveChanges(currentNote)
        viewModel.deleteNote()
        notesLiveData.value = NoteResult.Error(testData)

        assertEquals(result, testData)
    }
}