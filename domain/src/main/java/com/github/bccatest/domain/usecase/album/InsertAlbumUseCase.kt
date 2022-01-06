package com.github.bccatest.domain.usecase.album

import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.data.repository.AlbumRepository
import io.reactivex.Single

class InsertAlbumUseCase(private val repository: AlbumRepository) {
    fun execute(album: AlbumEntity): Single<Long> = repository.insertAlbum(album)
}
