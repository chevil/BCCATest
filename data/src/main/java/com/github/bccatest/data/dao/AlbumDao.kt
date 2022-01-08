package com.github.bccatest.data.dao

import androidx.room.*
import com.github.bccatest.data.model.AlbumEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: AlbumEntity): Single<Long>

    @Query("SELECT * FROM albums ORDER BY id")
    fun getAllAlbums(): Flowable<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE title LIKE :search ORDER BY id")
    fun getAlbumsByContent(search: String): Single<List<AlbumEntity>>

    @Query("DELETE FROM albums")
    fun deleteAllAlbums(): Single<Int>

}
