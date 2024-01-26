package com.nathanfremont.beersapp.beerlist

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.nathanfremont.beersapp.common.ComposeOverlay
import com.nathanfremont.beersapp.common.toReadableString
import com.nathanfremont.beersapp.ui.theme.BeersAppTheme
import com.nathanfremont.domain.Beer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun BeerListScreen(
    beerListViewModel: BeerListViewModel = hiltViewModel()
) {
    val beerListState: BeerListState by beerListViewModel
        .state
        .collectAsState()

    var mutableTextFieldValue by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(key1 = true) {
//        beerListViewModel.searchBeersMatchingFood(null)
//        beerListViewModel.getBeersMatchingFoodForPage(
//            matchingFood = mutableTextFieldValue,
//            page = 1,
//        )
    }

    val beerLazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(beerLazyListState) {
        snapshotFlow { beerLazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .map { index ->
                Timber.d(
                    "beerLazyListState ${"index" to index}" +
                            "${"beerListState.beers.size" to beerListState.beers.size}"
                )
                beerListState.beers.isNotEmpty()
                        && index >= (beerListState.beers.size - 5)
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                beerListViewModel.loadMoreBeers()
            }
    }

    BeerListContent(
        beerListState = beerListState,
        textFieldValue = mutableTextFieldValue,
        textFieldOnValueChange = { newValue: String ->
            mutableTextFieldValue = newValue.ifBlank {
                null
            }
            beerListViewModel.searchBeersMatchingFood(
                matchingFood = mutableTextFieldValue
            )
            coroutineScope.launch {
                beerLazyListState.scrollToItem(0)
            }
        },
        beerLazyListState = beerLazyListState,
    )
}

@Composable
private fun BeerListContent(
    beerListState: BeerListState,
    textFieldValue: String?,
    textFieldOnValueChange: (newValue: String) -> Unit,
    beerLazyListState: LazyListState = rememberLazyListState(),
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            TextField(
                value = textFieldValue ?: "",
                onValueChange = textFieldOnValueChange,
                modifier = Modifier
                    .fillMaxWidth(),
            )

            LazyColumn(
                state = beerLazyListState,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                    ),
            ) {
                items(beerListState.beers) { beer: Beer ->
                    BeerItem(beer = beer)
                }
            }

        }

        if (beerListState.isLoading) {
            ComposeOverlay {
                CircularProgressIndicator()
            }
        }

        LaunchedEffect(key1 = beerListState.error) {
            if (beerListState.error != null) {
                Toast.makeText(
                    context,
                    beerListState.error.errorTitle.toReadableString(context),
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }
}

@Composable
private fun BeerItem(
    beer: Beer,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            SubcomposeAsyncImage(
                model = beer.imageUrl,
                contentDescription = null,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        CircularProgressIndicator()
                    }
                },
                modifier = Modifier
                    .aspectRatio(1f),
            )
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = beer.name,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle.Default.copy(
                        fontSize = 18.sp,
                        color = Color.White,
                    ),
                )
                Text(
                    text = beer.description,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle.Default.copy(
                        fontSize = 14.sp,
                    ),
                )
            }
        }
    }
}

@Composable
@Preview
private fun BeerListContentIsLoadingPreview() {
    BeersAppTheme {
        BeerListContent(
            beerListState = BeerListState(
                isLoading = true,
                error = null,
                beers = emptyList(),
            ),
            textFieldValue = "textFieldValue",
            textFieldOnValueChange = {},
        )
    }
}

@Composable
@Preview
private fun BeerListContentWithBeersPreview() {
    BeersAppTheme {
        BeerListContent(
            beerListState = BeerListState(
                isLoading = false,
                error = null,
                beers = listOf(
                    Beer(
                        name = "Cheri Kinney",
                        description = "ubique",
                        imageUrl = "http://www.bing.com/search?q=dicam",
                    ),
                    Beer(
                        name = "Abraham Harvey",
                        description = "proin",
                        imageUrl = "https://www.google.com/#q=electram",
                    )
                ),
            ),
            textFieldValue = "textFieldValue",
            textFieldOnValueChange = {},
        )
    }
}