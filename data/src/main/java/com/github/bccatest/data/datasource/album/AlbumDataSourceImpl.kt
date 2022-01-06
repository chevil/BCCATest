package com.github.bccatest.data.datasource.album

import com.github.bccatest.data.fetch.AlbumListFetcher
import com.github.bccatest.data.dao.AlbumDao
import com.github.bccatest.data.model.AlbumEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import android.content.Context

class AlbumDataSourceImpl(private val albumDao: AlbumDao): AlbumDataSource {
    override fun fetchAllAlbums(context : Context): Flowable<List<AlbumEntity>> = AlbumListFetcher.fetchAllAlbums(context)
    override fun getAllAlbums(): Flowable<List<AlbumEntity>> = albumDao.getAllAlbums()
    override fun getSearchAlbums(search: String): Single<List<AlbumEntity>> = albumDao.getAlbumsByContent(search)
    override fun insertAlbum(album: AlbumEntity): Single<Long> = albumDao.insertAlbum(album)
    override fun deleteAllAlbums(): Single<Int> = albumDao.deleteAllAlbums()
}
