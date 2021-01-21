package com.karine.retrieve

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karine.retrieve.ui.DeleteUserObjectViewModel
import com.karine.retrieve.ui.SaveUserObjectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class DeleteUserObjectViewModelTest {

    private lateinit var viewModelDelete : DeleteUserObjectViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread" )

    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setupViewModel() {
        Dispatchers.setMain(mainThreadSurrogate)
        //use the fake user repo for the test
        viewModelDelete = DeleteUserObjectViewModel(DeleteUserObjectRepositoryImplTest())
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
    @Test
    fun checkUserObjectSaveFind() = runBlocking {
        viewModelDelete.deleteUserObjectFindToFirestore(LiveDataTestUtil.testUserObject)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModelDelete.snackbarMessage)
        Assert.assertEquals (snackbarMessage, R.string.createdUO)

    }
    @Test
    fun checkUserObjectSaveLost() = runBlocking {
        viewModelDelete.deleteUserObjectLostToFirestore(LiveDataTestUtil.testUserObject)
        val snackbarMessage = LiveDataTestUtil.getValue(viewModelDelete.snackbarMessage)
        Assert.assertEquals (snackbarMessage, R.string.createdUO)

    }
}