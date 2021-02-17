package com.example.keepnotes.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.keepnotes.R
import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.note.NoteActivity
import com.example.keepnotes.ui.note.NoteViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

    private val viewModel = mockk<MainViewModel>(relaxed = true)

    private val viewStateLiveData = MutableLiveData<MainViewState>()
    private val testNotes = listOf(
        Note("333", "title", "text"),
        Note("444", "title1", "text1"),
        Note("555", "title2", "text2")
    )

    @Before
    fun setUp() {
        startKoin { modules() }

        loadKoinModules(
            listOf(
                module {
                    viewModel { viewModel }
                    viewModel { mockk<NoteViewModel>(relaxed = true) }
                })
        )

        every { viewModel.getViewState() } returns viewStateLiveData

        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.listNotes))
            .perform(scrollToPosition<RecyclerAdapter.ViewHolder>(1))
        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }

    @Test
    fun check_detail_activity_intent_sent() {
        onView(withId(R.id.listNotes))
            .perform(actionOnItemAtPosition<RecyclerAdapter.ViewHolder>(1, click()))

        intended(
            allOf(
                hasComponent(NoteActivity::class.java.name),
                hasExtra(EXTRA_NOTE, testNotes[1].id)
            )
        )
    }

    @Test
    fun should_call_deleteNote() {
        val currentNote = testNotes[1]
        onView(withId(R.id.listNotes))
            .perform(actionOnItemAtPosition<RecyclerAdapter.ViewHolder>(1, longClick()))
        onView(withText(R.string.deleteNote)).perform(click())
        onView(withText(R.string.button_ok)).perform(click())
        verify(exactly = 1) { viewModel.deleteNote(currentNote.id) }
    }

}