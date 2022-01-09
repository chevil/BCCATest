package com.github.bccatest

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import org.junit.runner.RunWith

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.github.bccatest.data.database.AppDatabase
import com.github.bccatest.data.dao.AlbumDao
import com.github.bccatest.utils.Constants
import com.github.bccatest.AlbumApplication
import com.github.bccatest.data.model.AlbumEntity

import androidx.room.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

import android.util.Log

import kotlinx.coroutines.runBlocking

/**
 * Room inserting/updating album test
 *
 * Created by chevil on 09/01/2022
 */
@RunWith(AndroidJUnit4::class)
class RoomInsertTest {

    private lateinit var db: AppDatabase
    private val album : AlbumEntity = AlbumEntity(1,1L,"dummy","dummy","dummy")
    private val album2 : AlbumEntity = AlbumEntity(1,1L,"update","dummy","dummy")
    private var mBusy : Boolean = false
    private lateinit var subscription : Disposable

    companion object {
      private var mExpectedSize : Int = 0
      private var mExpectedName : String = "dummy" 
    }

    @Before
    fun init() {
        Log.v( Constants.LOGTAG, "Opening room database..." )
        db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java, "test")
                .allowMainThreadQueries() 
                .build()
        Log.v( Constants.LOGTAG, "Opened room database..." )
    }

    @After
    fun cleanup() {
      try {
        if ( db.isOpen ) { 
           Log.v( Constants.LOGTAG, "Closing database..." )
           db.close()
           Log.v( Constants.LOGTAG, "Closed database." )
        }
      } catch (e: Exception) {
        Log.v( Constants.LOGTAG, "Couldn't close database ${e.message}" )
      }
    }

    fun testDeleteAll() = runBlocking {
        var result : Int 

        Log.v( Constants.LOGTAG, "Testing delete all..." )
        mBusy = true
        mExpectedSize = 0
        db.albumDao().deleteAllAlbums()
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            result = it
                            Log.v( Constants.LOGTAG, "Delete all returned : ${result} : expected : ${mExpectedSize}" )
                            assertEquals(mExpectedSize, result)
                            mBusy = false
                        }
                      )
    }

    fun testIsEmpty() = runBlocking {
        Log.v( Constants.LOGTAG, "Testing if empty..." )
        var albums : List<AlbumEntity> 

        mBusy = true
        mExpectedSize = 0
        subscription = db.albumDao().getAllAlbums()
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            albums = it
                            Log.v( Constants.LOGTAG, "Test is empty retuned : ${albums.size} : expected : ${mExpectedSize}" )
                            assertEquals(mExpectedSize, albums.size)
                            mBusy = false
                            subscription.dispose()
                        }
                      )
    }

    fun testInsertAlbum() = runBlocking {
        Log.v( Constants.LOGTAG, "Inserting album..." )
        var result : Long 

        mBusy = true
        db.albumDao().insertAlbum(album)
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            result = it
                            assertEquals(1, result)
                            Log.v( Constants.LOGTAG, "Insert album returned : ${result}" )
                            mBusy = false
                        }
                      )
    }

    fun testAlbumInserted() = runBlocking {
        Log.v( Constants.LOGTAG, "Test album inserted..." )
        var albums : List<AlbumEntity>

        mBusy = true
        mExpectedName = "dummy"
        subscription = db.albumDao().getAllAlbums()
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            albums = it
                            val albumdb = albums[0]
                            Log.v( Constants.LOGTAG, "Album inserted returned : ${albumdb.title} expected : ${mExpectedName}" )
                            assertEquals(mExpectedName, albumdb.title)
                            mBusy = false
                            subscription.dispose()
                        }
                      )
    }

    fun testUpdateAlbum() = runBlocking {
        Log.v( Constants.LOGTAG, "Updating album..." )
        var result : Long

        mBusy = true
        db.albumDao().insertAlbum(album2)
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            result = it
                            assertEquals(1, result)
                            Log.v( Constants.LOGTAG, "Update album returned : ${result}" )
                            mBusy = false
                        }
                      )
    }

    fun testAlbumUpdated() = runBlocking {
        Log.v( Constants.LOGTAG, "Test album updated..." )
        var albums : List<AlbumEntity>

        mBusy = true
        mExpectedName = "update"
        subscription = db.albumDao().getAllAlbums()
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            albums = it
                            val albumdb = albums[0]
                            Log.v( Constants.LOGTAG, "Album updated returned : ${albumdb.title} expected : ${mExpectedName}" )
                            assertEquals(mExpectedName, albumdb.title)
                            mBusy = false
                            subscription.dispose()
                        }
                      )
    }

    @Test
    fun testAlbumDatabase() {
        testDeleteAll()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
        testIsEmpty()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
        testInsertAlbum()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
        testAlbumInserted()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
        testUpdateAlbum()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
        testAlbumUpdated()
        do {
           Thread.sleep(100)
           Log.v( Constants.LOGTAG, "Waiting result..." )
        } while (mBusy)
    }

}
