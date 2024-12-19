package com.ayukrisna.dicodingstory.di

import androidx.room.Room
import com.ayukrisna.dicodingstory.data.local.database.StoryDatabase
import com.ayukrisna.dicodingstory.view.ui.screen.auth.login.LoginViewModel
import com.ayukrisna.dicodingstory.view.ui.screen.auth.signup.SignupViewModel
import com.ayukrisna.dicodingstory.data.local.pref.UserPreference
import com.ayukrisna.dicodingstory.data.repository.UserRepositoryImp
import com.ayukrisna.dicodingstory.data.repository.StoryRepositoryImp
import com.ayukrisna.dicodingstory.domain.repository.StoryRepository
import com.ayukrisna.dicodingstory.domain.repository.UserRepository
import com.ayukrisna.dicodingstory.domain.usecase.AddStoryUseCase
import com.ayukrisna.dicodingstory.domain.usecase.DetailStoryUseCase
import com.ayukrisna.dicodingstory.domain.usecase.ListStoryUseCase
import com.ayukrisna.dicodingstory.domain.usecase.LoginUseCase
import com.ayukrisna.dicodingstory.domain.usecase.LogoutUseCase
import com.ayukrisna.dicodingstory.util.provideDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import com.ayukrisna.dicodingstory.domain.usecase.RegisterUseCase
import com.ayukrisna.dicodingstory.util.FileHelper
import com.ayukrisna.dicodingstory.view.ui.screen.story.addstory.AddStoryViewModel
import com.ayukrisna.dicodingstory.view.ui.screen.story.detailstory.DetailStoryViewModel
import com.ayukrisna.dicodingstory.view.ui.screen.story.liststory.ListStoryViewModel
import com.ayukrisna.dicodingstory.view.ui.screen.auth.splash.SplashViewModel
import com.ayukrisna.dicodingstory.view.ui.screen.maps.MapsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

// Database Module
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java,
            "story_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<StoryDatabase>().storyDao() }
}


// File Helper Module
val fileHelperModule = module {
    single { FileHelper(androidContext()) }
}

// Data Store Module
val dataStoreModule = module {
    single { provideDataStore(get()) }
}

// Preference
val preferenceModule = module {
    single { UserPreference(get()) }
}

// Repository
var repositoryModules : Module = module {
    singleOf(::UserRepositoryImp) bind UserRepository::class
    singleOf(::StoryRepositoryImp) bind StoryRepository::class
}

// Use Case
val useCaseModules = module {
    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { ListStoryUseCase(get()) }
    single { DetailStoryUseCase(get()) }
    single { AddStoryUseCase(get()) }
}

//View Model
val viewModelModules = module {
    viewModel{ SplashViewModel(get()) }
    viewModel{ SignupViewModel(get()) }
    viewModel{ LoginViewModel(get()) }
    viewModel{ ListStoryViewModel(get()) }
    viewModel{ MapsViewModel(get()) }
    viewModel{ DetailStoryViewModel(get()) }
    viewModel{ AddStoryViewModel(get(), get()) }
}