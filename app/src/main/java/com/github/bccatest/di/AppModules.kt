package com.github.bccatest.di

import androidx.room.Room
import com.github.bccatest.data.dao.AlbumDao
import com.github.bccatest.data.database.AppDatabase
import com.github.bccatest.data.datasource.album.AlbumDataSource
import com.github.bccatest.data.datasource.album.AlbumDataSourceImpl
import com.github.bccatest.data.repository.AlbumRepositoryImpl
import com.github.bccatest.data.repository.AlbumRepository
import com.github.bccatest.domain.usecase.album.FetchAllAlbumsUseCase
import com.github.bccatest.domain.usecase.album.GetAllAlbumsUseCase
import com.github.bccatest.domain.usecase.album.GetSearchAlbumsUseCase
import com.github.bccatest.domain.usecase.album.InsertAlbumUseCase
import com.github.bccatest.domain.usecase.album.DeleteAllAlbumsUseCase
import com.github.bccatest.viewmodel.AlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AlbumViewModel(get(),get(),get(),get(),get()) }
}

val repositoryModule: Module = module {
    single<AlbumRepository> { AlbumRepositoryImpl(get()) }
}

val localDataModule: Module = module {
    single<AlbumDataSource> { AlbumDataSourceImpl(get()) }
    single<AlbumDao> { get<AppDatabase>().albumDao() }
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "Albums.db"
        )
        .build()
   }
}

val useCaseModule: Module = module {
    // Room
    single<InsertAlbumUseCase> { InsertAlbumUseCase(get()) }
    single<FetchAllAlbumsUseCase> { FetchAllAlbumsUseCase(get()) }
    single<GetAllAlbumsUseCase> { GetAllAlbumsUseCase(get()) }
    single<GetSearchAlbumsUseCase> { GetSearchAlbumsUseCase(get()) }
    single<DeleteAllAlbumsUseCase> { DeleteAllAlbumsUseCase(get()) }
}

val apiModule: Module = module {
    //..
}

