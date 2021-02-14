package com.example.keepnotes.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.keepnotes.model.*
import com.example.keepnotes.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.*

import org.junit.Assert.*

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val userLiveData = MutableLiveData<User>()
    private val testUser = User("name", "email")

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        every { mockRepository.getCurrentUser() } returns userLiveData
        viewModel = SplashViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should call getCurrentUser once`() {
        viewModel.requestUser()
        verify(exactly = 1) { mockRepository.getCurrentUser() }
    }

    @Test
    fun `should return SplashViewState with isAuth = true`() {
        var result: Boolean? = null

        viewModel.getViewState().observeForever { result = it?.data }
        viewModel.requestUser()
        userLiveData.value = testUser

        assertEquals(result, true)
    }

    @Test
    fun `should return SplashViewState with error`() {
        var result: Throwable? = null

        viewModel.getViewState().observeForever { result = it.error }
        viewModel.requestUser()
        userLiveData.value = null

        assertEquals(result?.javaClass, NoAuthException::class.java)
    }
}