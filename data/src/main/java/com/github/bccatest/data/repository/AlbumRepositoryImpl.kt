package com.github.bccatest.data.repository

import com.github.bccatest.data.datasource.album.AlbumDataSource
import com.github.bccatest.data.model.AlbumEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import android.content.Context

class AlbumRepositoryImpl(private val albumDataSource: AlbumDataSource): AlbumRepository {
    override fun fetchAllAlbums(context : Context): Flowable<List<AlbumEntity>>
        = albumDataSource.fetchAllAlbums(context)

    override fun getAllAlbums(): Flowable<List<AlbumEntity>>
        = albumDataSource.getAllAlbums().flatMap { albums-> Flowable.just(albums) }

    override fun getSearchAlbums(search: String): Single<List<AlbumEntity>>
        = albumDataSource.getSearchAlbums(search).flatMap { albums -> Single.just(albums) }

    override fun insertAlbum(album: AlbumEntity): Single<Long> = albumDataSource.insertAlbum(album)

    override fun deleteAllAlbums(): Single<Int> = albumDataSource.deleteAllAlbums()
}
