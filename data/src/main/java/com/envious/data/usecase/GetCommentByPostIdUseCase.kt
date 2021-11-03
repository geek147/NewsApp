package com.envious.data.usecase

import com.envious.domain.model.Comment
import com.envious.domain.model.UserDetail
import com.envious.domain.repository.PostRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetCommentByPostIdUseCase @Inject constructor(
    private val repository: PostRepository
) : BaseCaseWrapper<List<Comment>, GetCommentByPostIdUseCase.Params>() {

    override suspend fun build(params: Params?): Result<List<Comment>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")

        return repository.getCommentByPostId(params.postId)
    }

    class Params(val postId: Int)
}
