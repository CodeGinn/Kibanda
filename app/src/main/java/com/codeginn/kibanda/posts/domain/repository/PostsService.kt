package com.codeginn.kibanda.posts.domain.repository

import com.codeginn.kibanda.posts.domain.model.PostsRequest
import com.codeginn.kibanda.posts.domain.model.PostsResponse
import com.codeginn.kibanda.utils.Result

//typealias  PostsList = List<PostsResponse>
//typealias PostsListResult = Result<PostsList>
interface PostsService {
    suspend fun getPosts(): List<PostsResponse>
}