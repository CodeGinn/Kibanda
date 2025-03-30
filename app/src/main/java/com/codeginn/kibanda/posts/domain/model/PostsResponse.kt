package com.codeginn.kibanda.posts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostsResponse(
    val userId: Int,
    val title: String,
    val body: String
)
