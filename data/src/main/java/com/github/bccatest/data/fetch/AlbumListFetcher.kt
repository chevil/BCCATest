package com.github.bccatest.data.fetch

/**
 * A Class with a main function to fetch the AlbumEntities
 * using OkHttp and RxJava
 * and stores them in the database
 */

import android.widget.Toast
import android.util.Log
import android.content.Context

import com.github.bccatest.data.model.AlbumEntity

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

// okhttp
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.OkHttpClient

// rxJava
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class AlbumListFetcher {

  companion object {

     private lateinit var mContext : Context
     private var newList : Flowable<List<AlbumEntity>> = Flowable.just(listOf())

     /*
      * static function to get the albums list and return it to the use case
      */
     fun fetchAllAlbums(context : Context) : Flowable<List<AlbumEntity>> {

      mContext = context
      try {

        // build a specific client to debug requests
        var client : OkHttpClient = OkHttpClient.Builder()
                              .addInterceptor(HttpLoggingInterceptor().apply {
                                 level = Level.NONE
                              })
                              .build();

        // build the retrofit service using a Gson converter
        // and a RxJava adapter to get result as an observable
        var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        // build the object to process the requests towards the API
        var service = retrofit.create(AlbumService::class.java)

        // call the service on a new thread (not the UI thread )
        // but the result should be processed on the main UI thread
        // the result is then processed by handleUsersList
        service.getAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .blockingSubscribe(this::handleAlbumList, this::handleRxError)

      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't fetch albums", e )
      }

      return newList
    }

    // This function handle the response from the retrofit call : a list of albums
    private fun handleAlbumList(albumList: List<AlbumEntity>) {
      try {
        if ( albumList.size > 0 )
        {
           Log.v( Constants.LOGTAG, "Got albums : " + albumList.size )
           newList = Flowable.just(albumList)
        }
      } catch ( e: Exception ) {
        Log.e( Constants.LOGTAG, "Couldn't process albums list", e )
      }
    }

    // This function is called when Rx returns an error,
    // so we use the cached data ( if any )
    private fun handleRxError(t: Throwable) {
        Log.e( Constants.LOGTAG, "Couldn't fetch albums list", t )
        newList = Flowable.just(listOf())
    }

  }
}
