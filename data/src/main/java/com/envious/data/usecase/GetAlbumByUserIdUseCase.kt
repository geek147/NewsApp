package com.envious.data.usecase

import com.envious.domain.model.Album
import com.envious.domain.repository.PostRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetAlbumByUserIdUseCase @Inject constructor(
    private val repository: PostRepository
) : BaseCaseWrapper<List<Album>, GetAlbumByUserIdUseCase.Params>() {

    override suspend fun build(params: Params?): Result<List<Album>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")

        return repository.getAlbumByUserId(params.albunId)
    }

    class Params(val albunId: Int)
}
