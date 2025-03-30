@file:OptIn(ExperimentalMaterial3Api::class)

package com.codeginn.kibanda.products.presentation.screen.tab

import android.R.attr.singleLine
import android.R.attr.textStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codeginn.kibanda.products.data.local.categoryList
import com.codeginn.kibanda.products.domain.model.Product
import com.codeginn.kibanda.products.presentation.screen.pages.FruitsScreen
import com.codeginn.kibanda.products.presentation.screen.pages.VegetablesScreen
import com.codeginn.kibanda.products.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeTab(
    modifier: Modifier = Modifier,
    onProductCardClicked: (Product) -> Unit = {},
    onSearch: () -> Unit = {}
){
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            HomeTopBar(onSearch = onSearch)
        }
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {

            var selectedTabIndex by rememberSaveable {
                mutableIntStateOf(0)
            }

            val pagerState = rememberPagerState { categoryList.size }

            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }
            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
//                if (!pagerState.isScrollInProgress){
//                    selectedTabIndex = pagerState.currentPage
//                }
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                categoryList.forEachIndexed { index, category ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        }
                    ) {
                        Text(
                            text = category.categoryName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.height(48.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
            HorizontalPager(
                state = pagerState
            ) {page ->
                when(page){
                    0 -> FruitsScreen(
                        onProductCardClicked = onProductCardClicked
                    )
                    1 -> VegetablesScreen(
                        onProductCardClicked = onProductCardClicked
                    )
                }

            }

        }
    }

}

@Composable
fun HomeTopBar(
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    onSearch: () -> Unit = {}
){
    val searchText by searchViewModel.searchText.collectAsState()

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = searchText,
                    onValueChange =
                        searchViewModel::onSearchTextChange,

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
                    IconButton(
                        onClick = {
                            if (searchText.isNotEmpty()){
                                onSearch()
                            }

                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Button"
                        )
                    }
                },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .height(48.dp)
                        .width(200.dp)
                )
            }

        }/*,
        actions = {
            BadgedBox(
                badge = {
                    Text(
                        text = if (cartItems.isNotEmpty()){
                            cartItems.size.toString()
                        } else {
                            "0"
                        }

                    )
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                IconButton(
                    onClick = onCartIndicatorClicked,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart"
                    )
                }
            }
        }*/

    )
}



