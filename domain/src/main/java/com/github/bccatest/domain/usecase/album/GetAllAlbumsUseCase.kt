package com.github.bccatest.domain.usecase.album

import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.data.repository.AlbumRepository
import io.reactivex.Flowable

class GetAllAlbumsUseCase(private val repository: AlbumRepository) {
    fun execute(): Flowable<List<AlbumEntity>> = repository.getAllAlbums()
}
