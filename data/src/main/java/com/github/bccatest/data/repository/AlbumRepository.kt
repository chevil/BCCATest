package com.github.bccatest.data.repository

import com.github.bccatest.data.model.AlbumEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import android.content.Context

interface AlbumRepository {
    fun fetchAllAlbums(context : Context): Flowable<List<AlbumEntity>> 
    fun getAllAlbums(): Flowable<List<AlbumEntity>>
    fun getSearchAlbums(search: String): Single<List<AlbumEntity>>
    fun insertAlbum(album: AlbumEntity): Single<Long>
    fun deleteAllAlbums(): Single<Int>
}
