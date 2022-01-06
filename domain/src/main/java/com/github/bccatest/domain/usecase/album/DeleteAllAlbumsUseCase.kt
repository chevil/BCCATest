package com.github.bccatest.domain.usecase.album

import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.data.repository.AlbumRepository
import io.reactivex.Single

class DeleteAllAlbumsUseCase(private val repository: AlbumRepository) {
    fun execute(): Single<Int> = repository.deleteAllAlbums()
}
