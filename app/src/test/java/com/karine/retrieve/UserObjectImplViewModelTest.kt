package com.karine.retrieve

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karine.retrieve.LiveDataTestUtil.testUserObject
import com.karine.retrieve.ui.mainPage.UserObjectImplViewModel
import com.karine.retrieve.utils.TaskExt
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

class UserObjectImplViewModelTest {
    private lateinit var viewModel: UserObjectImplViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread" )

    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

   @ExperimentalCoroutinesApi
   @Before
   fun setupViewModel() {
       Dispatchers.setMain(mainThreadSurrogate)
       //use the fake user repo for the test
       viewModel = UserObjectImplViewModel(UserObjectRepositoryImplTest())
   }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
    @Test
    fun checkUserObjectSaveFind() = runBlocking {
        viewModel.saveUserObjectFindToFirestore(testUserObject.docId, testUserObject.uid,
            testUserObject.created, testUserObject.pseudo, testUserObject.email, testUserObject.phone,
        testUserObject.date, testUserObject.type, testUserObject.address, testUserObject.postalCode,
        testUserObject.city, testUserObject.description, testUserObject.photo)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModel.snackbarMessage)
        assertEquals (snackbarMessage, R.string.createdUO)

    }
    @Test
    fun checkUserObjectSaveLost() = runBlocking {
        viewModel.saveUserObjectLostToFirestore(testUserObject.docId, testUserObject.uid,
            testUserObject.created, testUserObject.pseudo, testUserObject.email, testUserObject.phone,
            testUserObject.date, testUserObject.type, testUserObject.address, testUserObject.postalCode,
            testUserObject.city, testUserObject.description, testUserObject.photo)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModel.snackbarMessage)
        assertEquals (snackbarMessage, R.string.createdUO)

    }
}