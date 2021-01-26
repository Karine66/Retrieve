package com.karine.retrieve.injections

import com.karine.retrieve.repositories.DeleteUserObjectRepository
import com.karine.retrieve.repositories.DeleteUserObjetRepositoryImpl
import com.karine.retrieve.repositories.SaveUserObjectRepository
import com.karine.retrieve.repositories.SaveUserObjectRepositoryimpl
import com.karine.retrieve.ui.DeleteUserObjectViewModel
import com.karine.retrieve.ui.SaveUserObjectViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appModule = module {

    // single instance of HelloRepository
    single<SaveUserObjectRepository> { SaveUserObjectRepositoryimpl() }
    single<DeleteUserObjectRepository> { DeleteUserObjetRepositoryImpl() }

    // MyViewModel ViewModel
    viewModel { SaveUserObjectViewModel(get()) }
    viewModel { DeleteUserObjectViewModel(get())}
}