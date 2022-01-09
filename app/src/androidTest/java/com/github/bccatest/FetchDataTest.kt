package com.github.bccatest

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import org.junit.runner.RunWith

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.github.bccatest.data.fetch.AlbumListFetcher
import com.github.bccatest.utils.Constants
import com.github.bccatest.AlbumApplication
import com.github.bccatest.data.model.AlbumEntity

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
class FetchDataTest {

    private lateinit var subscription : Disposable

    @Before
    fun init() {
    }

    @After
    fun cleanup() {
    }

    @Test
    fun testFetchData() = runBlocking {
        var albums : List<AlbumEntity> 

        Log.v( Constants.LOGTAG, "Fetching all albums..." )
        subscription = AlbumListFetcher.fetchAllAlbums(ApplicationProvider.getApplicationContext())
                     .subscribeOn(AndroidSchedulers.mainThread())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(
                        { it ->
                            albums = it
                            Log.v( Constants.LOGTAG, "Fetch all albums returned : ${albums.size}" )
                            assertEquals(5000, albums.size)
                            subscription.dispose()
                        }
                      )
    }

}
