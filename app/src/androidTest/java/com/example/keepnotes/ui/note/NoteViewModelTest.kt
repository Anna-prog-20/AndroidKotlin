package com.example.keepnotes.ui.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.example.keepnotes.R
import com.example.keepnotes.model.Color
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository
import com.example.keepnotes.model.getColorInt
import io.mockk.*

import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = spyk(NoteViewModel(mockk<Repository>()))
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("1", "title test note", "text test note")

    @Before
    fun setUp() {
        startKoin { modules() }
        loadKoinModules(listOf(
            module {
                viewModel { viewModel }
            }
        ))

        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } just runs

        activityTestRule.launchActivity(Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        })
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(ViewMatchers.withId(R.id.itemSelectionColorNote)).perform(click())
        onView(ViewMatchers.withId(R.id.color_picker))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(ViewMatchers.withId(R.id.itemSelectionColorNote))
            .perform(click()).perform(click())
        onView(ViewMatchers.withId(R.id.color_picker))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(ViewMatchers.withId(R.id.itemSelectionColorNote)).perform(click())
        onView(ViewMatchers.withTagValue(Matchers.`is`(Color.BLUE)))
            .perform(click())

        val colorInt = Color.BLUE.getColorInt(activityTestRule.activity)

        onView(ViewMatchers.withId(R.id.toolbar)).check { view, _ ->
            assertTrue(
                "toolbar background color does not match",
                (view.background as? ColorDrawable)?.color == colorInt
            )
        }
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { viewModel.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(ViewMatchers.withId(R.id.titleEt))
            .check(ViewAssertions.matches(ViewMatchers.withText(testNote.topic)))
        onView(ViewMatchers.withId(R.id.textEt))
            .check(ViewAssertions.matches(ViewMatchers.withText(testNote.text)))
    }

    @Test
    fun should_call_saveNote() {
        onView(ViewMatchers.withId(R.id.titleEt))
            .perform(ViewActions.typeText(testNote.topic))
        onView(ViewMatchers.withId(R.id.textEt))
            .perform(ViewActions.typeText(testNote.text))
        verify(timeout = 1000) { viewModel.saveChanges(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
        onView(ViewMatchers.withText(R.string.deleteNote)).perform(click())
        onView(ViewMatchers.withText(R.string.button_ok)).perform(click())
        verify(exactly = 1) { viewModel.deleteNote() }
    }
}