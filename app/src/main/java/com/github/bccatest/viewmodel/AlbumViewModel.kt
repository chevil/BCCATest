package com.github.bccatest.viewmodel

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.github.bccatest.base.BaseViewModel
import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.AlbumApplication
import com.github.bccatest.domain.usecase.album.FetchAllAlbumsUseCase
import com.github.bccatest.domain.usecase.album.GetAllAlbumsUseCase
import com.github.bccatest.domain.usecase.album.GetSearchAlbumsUseCase
import com.github.bccatest.domain.usecase.album.InsertAlbumUseCase
import com.github.bccatest.domain.usecase.album.DeleteAllAlbumsUseCase
import com.github.bccatest.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.util.Log
import android.content.Context

class AlbumViewModel(
    private val fetchAllAlbumsUseCase: FetchAllAlbumsUseCase,
    private val getAllAlbumsUseCase: GetAllAlbumsUseCase,
    private val insertAlbumUseCase: InsertAlbumUseCase,
    private val getSearchAlbumsUseCase: GetSearchAlbumsUseCase,
    private val deleteAllAlbumsUseCase: DeleteAllAlbumsUseCase
) : BaseViewModel() {

    val albumListObservable: MutableStateFlow<List<AlbumEntity>> = MutableStateFlow(ArrayList())
    val albumListSeeable: MutableStateFlow<List<AlbumEntity>> = MutableStateFlow(ArrayList())
    var mContext : Context
    var mBusy : Boolean = false
    var isLiveData : Int = -1

    init {
        mContext = AlbumApplication.applicationContext()
        fetchAllAlbums()
    }

    // indicates if data is live or cached
    fun isLive() : Int {
        return isLiveData
    }

    // filter albums
    fun filterAlbums(search: String) : Unit {
        var fList : List<AlbumEntity> = ArrayList()
        Log.v( Constants.LOGTAG, "Filtering on ${search}" )
        albumListObservable.value.forEach() { album ->
           if ( album.title.contains( search ) || search == "" ) { 
              fList += album 
              // Log.v( Constants.LOGTAG, "Filtered on ${album.title}" )
           }
        }
        Log.v( Constants.LOGTAG, "Filtered ${fList.size} items" )
        albumListSeeable.value = fList
    }

    // insert an album in the database
    fun insertAlbum(album: AlbumEntity) {
      mBusy = true
      runBlocking {
          insertAlbumUseCase.execute(album)
              .subscribeOn(Schedulers.io())
              .observeOn(Schedulers.io())
              .doFinally { mBusy = false }
              .subscribe({ it ->
                  Log.v( Constants.LOGTAG, "inserted : ${it}" )
               }, 
               {
                  Log.v( Constants.LOGTAG, "insertion error : ${it}" )
               })
      }
    }

    // insert all albums from a list in the database
    fun insertAllAlbums(albums: List<AlbumEntity>) {
        albums.forEach { album ->
           Log.v( Constants.LOGTAG, "inserting : ${album.title}" )
           // waiting for previous insertion to finish
           // to avoid OutOfMemory error
           do {
              try {
                Thread.sleep(1)
              } catch ( e : InterruptedException ) {
                Log.v( Constants.LOGTAG, "sleep interrupted" )
              }
           } while ( mBusy )
           insertAlbum(album)
        }
    }

    // get all albums from the database
    private fun getAllAlbums() = CoroutineScope(Dispatchers.IO).launch {
        addDisposable(
            getAllAlbumsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    albumListObservable.value = it
                    albumListSeeable.value = it
                }, {
                })
        )
    }

    // delete all albums in the database
    private fun deleteAllAlbums() = CoroutineScope(Dispatchers.IO).launch {
        addDisposable(
            deleteAllAlbumsUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    Log.v( Constants.LOGTAG, "Deleted all albums" );
                }, {
                })
        )
    }

    // fetch all albums from the network
    private fun fetchAllAlbums() = CoroutineScope(Dispatchers.IO).launch {
        addDisposable(
            fetchAllAlbumsUseCase.execute(AlbumApplication.applicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                   // we show the list at once for performance concerns
                   albumListObservable.value = it
                   albumListSeeable.value = it
                   // when reading from network fails, read from the local database
                   Log.v( Constants.LOGTAG, "Fetch returned : ${albumListObservable.value.size}" )
                   if ( albumListObservable.value.size <= 0 )  {
                      isLiveData = 0
                      getAllAlbums()
                   } else {
                      isLiveData = 1
                      // now, the list is shown 
                      // we can update the database with co-routines
                      // Albums are created or updated because on the OnConflictStrategy.REPLACE
                      insertAllAlbums(albumListObservable.value)
                   }
                 }, {})
        )
    }

    // get albums from the database with a search criteria
    fun getSearchAlbums(search: String) = CoroutineScope(Dispatchers.IO).launch {
        Log.v( Constants.LOGTAG, "Searching : ${search}" )
        addDisposable(
            getSearchAlbumsUseCase.execute(search)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    Log.v( Constants.LOGTAG, "Search returned ${it.size} result(s)" )
                    albumListSeeable.value = it
                }, 
                {
                    Log.v( Constants.LOGTAG, "No results" )
                })
        )
    }
}
