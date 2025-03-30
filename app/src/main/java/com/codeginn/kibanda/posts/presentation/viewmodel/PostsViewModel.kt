package com.codeginn.kibanda.posts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeginn.kibanda.posts.domain.model.PostsResponse
import com.codeginn.kibanda.posts.domain.repository.PostsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel(
    private val postService: PostsService
): ViewModel() {
    private val _posts = MutableStateFlow<List<PostsResponse>>(emptyList())
    val posts = _posts.asStateFlow()
    fun fetchPosts(){
        viewModelScope.launch {
            _posts.value = postService.getPosts()
        }
    }
}