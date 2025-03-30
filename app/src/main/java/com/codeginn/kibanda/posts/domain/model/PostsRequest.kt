package com.codeginn.kibanda.posts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostsRequest(
    val userId: Int,
    val id : Int,
    val title: String,
    val body: String
)
