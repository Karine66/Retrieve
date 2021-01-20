package com.karine.retrieve

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karine.retrieve.LiveDataTestUtil.testUserObject
import com.karine.retrieve.ui.SaveUserObjectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveUserObjectViewModelTest {
    private lateinit var viewModelSave: SaveUserObjectViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread" )

    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

   @ExperimentalCoroutinesApi
   @Before
   fun setupViewModel() {
       Dispatchers.setMain(mainThreadSurrogate)
       //use the fake user repo for the test
       viewModelSave = SaveUserObjectViewModel(SaveUserObjectRepositoryImplTest())
   }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
    @Test
    fun checkUserObjectSaveFind() = runBlocking {
        viewModelSave.saveUserObjectFindToFirestore(testUserObject.docId, testUserObject.uid,
            testUserObject.created, testUserObject.pseudo, testUserObject.email, testUserObject.phone,
        testUserObject.date, testUserObject.type, testUserObject.address, testUserObject.postalCode,
        testUserObject.city, testUserObject.description, testUserObject.photo)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModelSave.snackbarMessage)
        assertEquals (snackbarMessage, R.string.createdUO)

    }
    @Test
    fun checkUserObjectSaveLost() = runBlocking {
        viewModelSave.saveUserObjectLostToFirestore(testUserObject.docId, testUserObject.uid,
            testUserObject.created, testUserObject.pseudo, testUserObject.email, testUserObject.phone,
            testUserObject.date, testUserObject.type, testUserObject.address, testUserObject.postalCode,
            testUserObject.city, testUserObject.description, testUserObject.photo)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModelSave.snackbarMessage)
        assertEquals (snackbarMessage, R.string.createdUO)

    }
}