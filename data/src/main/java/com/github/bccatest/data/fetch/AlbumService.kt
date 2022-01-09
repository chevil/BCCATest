package com.github.bccatest.data.fetch

import com.github.bccatest.data.model.AlbumEntity

import retrofit2.Call;
import retrofit2.http.GET;

// rxJava
import io.reactivex.Observable

/**
 * Created by chevil on 07/01/2022.
 */

public interface AlbumService {

    @GET("technical-test.json")
    fun getAlbums() : Observable<List<AlbumEntity>>
}
