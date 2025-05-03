@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.products.presentation.screen.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.products.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>()
){
    val searchResults by searchViewModel.searchResults.collectAsState()
    val searchText by searchViewModel.searchText.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = searchViewModel::onSearchTextChange,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                text = "Search product...",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.fillMaxHeight()
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Button"
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .height(48.dp)
                            .width(200.dp)
                    )
                }
            )
        }
    ) {
        if (searchText.isNotBlank()){
            if (searchResults.isEmpty()) {
                Box(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(searchResults) {searchResult->
                        ProductCard(searchResult)
                    }
                }
            }
        }else {
            Text("Start typing to search for groceries.")
        }

    }



}