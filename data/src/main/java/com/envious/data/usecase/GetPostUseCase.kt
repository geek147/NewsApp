package com.envious.data.usecase

import com.envious.domain.model.Post
import com.envious.domain.repository.PostRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val repository: PostRepository
) : BaseCaseWrapper<List<Post>, Unit>() {

    override suspend fun build(params: Unit?): Result<List<Post>> {
        return repository.getPosts()
    }
}
