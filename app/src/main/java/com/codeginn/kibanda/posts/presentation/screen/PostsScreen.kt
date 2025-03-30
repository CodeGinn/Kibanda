package com.codeginn.kibanda.posts.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.posts.presentation.viewmodel.PostsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel = koinViewModel<PostsViewModel>()
){
    val posts by postsViewModel.posts.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()

    ) {
        Button(
            onClick = {
                postsViewModel.fetchPosts()
            }
        ) {
            Text("Fetch Posts")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .wrapContentSize()
                ) {
                    Text(post.userId.toString())
                    Text(post.title)
                    Text(post.body)
                }
            }
        }
    }

}