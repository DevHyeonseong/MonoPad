package com.project.monopad.di

import com.google.firebase.auth.FirebaseAuth
import com.project.monopad.network.remote.api.MovieRepoApi
import com.project.monopad.network.remote.datasource.MovieRemoteDataSource
import com.project.monopad.network.remote.datasource.MovieRemoteDataSourceImpl
import com.project.monopad.network.repository.MovieRepoImpl
import com.project.monopad.network.remote.datasource.*
import com.project.monopad.network.repository.*
import com.project.monopad.ui.viewmodel.DetailViewModel
import com.project.monopad.ui.viewmodel.LoginViewModel
import com.project.monopad.ui.viewmodel.MovieViewModel
import com.project.monopad.ui.viewmodel.RegisterViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

var networkModule = module{
    single<MovieRepoApi>{
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieRepoApi::class.java)
    }

    single{
        val b = OkHttpClient.Builder()  // 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
        b.build()
    }

    single{
        FirebaseAuth.getInstance()
    }
}

var remoteDataSource = module {
    single<MovieRemoteDataSource>{ MovieRemoteDataSourceImpl(get()) }
    single<UserRemoteDataSource>{ UserRemoteDataSourceImpl(get()) }
}

var localDataSource = module {
}

var repositoryModule = module {
    single { MovieRepoImpl(get()) }
    single { UserRepoImpl(get()) }
}

var viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { DetailViewModel(get())}
}

var monoDiModule = listOf(networkModule, remoteDataSource, repositoryModule, viewModelModule)