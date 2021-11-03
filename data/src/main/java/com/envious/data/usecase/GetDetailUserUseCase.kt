package com.envious.data.usecase

import com.envious.domain.model.UserDetail
import com.envious.domain.repository.PostRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetDetailUserUseCase @Inject constructor(
    private val repository: PostRepository
) : BaseCaseWrapper<List<UserDetail>, GetDetailUserUseCase.Params>() {

    override suspend fun build(params: Params?): Result<List<UserDetail>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")

        return repository.getUserDetailById(params.userId)
    }

    class Params(val userId: Int)
}
