package com.github.bccatest.domain.usecase.album

import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.data.repository.AlbumRepository
import io.reactivex.Flowable
import io.reactivex.Single

class GetSearchAlbumsUseCase(private val repository: AlbumRepository) {
    fun execute(search: String): Single<List<AlbumEntity>> = repository.getSearchAlbums(search)
}
