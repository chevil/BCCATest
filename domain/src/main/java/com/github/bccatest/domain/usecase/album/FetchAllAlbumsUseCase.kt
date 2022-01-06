package com.github.bccatest.domain.usecase.album

import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.data.repository.AlbumRepository
import io.reactivex.Flowable
import android.content.Context

class FetchAllAlbumsUseCase(private val repository: AlbumRepository) {
    fun execute(context : Context): Flowable<List<AlbumEntity>> = repository.fetchAllAlbums(context)
}
