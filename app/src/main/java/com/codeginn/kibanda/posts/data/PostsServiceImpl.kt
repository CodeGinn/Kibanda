package com.codeginn.kibanda.posts.data

import com.codeginn.kibanda.posts.domain.model.PostsResponse
import com.codeginn.kibanda.posts.domain.repository.PostsService
import com.codeginn.kibanda.posts.utils.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.request.url

class PostsServiceImpl(
    private val postClient: HttpClient
): PostsService {
    override suspend fun getPosts(): List<PostsResponse> {
        return postClient.get {
            url(HttpRoutes.POSTS)
        }.body()
    }
}