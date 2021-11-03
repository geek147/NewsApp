package com.envious.data.usecase

import com.envious.domain.model.Photo
import com.envious.domain.repository.PostRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetPhotosByAlbumIdUseCase @Inject constructor(
    private val repository: PostRepository
) : BaseCaseWrapper<List<Photo>, GetPhotosByAlbumIdUseCase.Params>() {

    override suspend fun build(params: Params?): Result<List<Photo>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")

        return repository.getPhotosByAlbumId(params.albumId)
    }

    class Params(val albumId: Int)
}
