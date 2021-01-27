package com.karine.retrieve.injections

import com.karine.retrieve.repositories.*
import com.karine.retrieve.ui.DeleteAllObjectCurrentUserViewModel
import com.karine.retrieve.ui.DeleteUserObjectViewModel
import com.karine.retrieve.ui.SaveUserObjectViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appModule = module {

    // single instance of Repository
    single<SaveUserObjectRepository> { SaveUserObjectRepositoryimpl() }
    single<DeleteUserObjectRepository> { DeleteUserObjetRepositoryImpl() }
    single<DeleteAllObjectFromCurrentUserRepository>{ DeleteAllObjectFromCurrentUserRepoImpl()}

    // MyViewModel ViewModel
    viewModel { SaveUserObjectViewModel(get()) }
    viewModel { DeleteUserObjectViewModel(get())}
    viewModel { DeleteAllObjectCurrentUserViewModel(get()) }
}