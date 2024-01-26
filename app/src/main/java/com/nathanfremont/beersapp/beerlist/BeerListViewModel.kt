package com.nathanfremont.beersapp.beerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanfremont.beersapp.IDispatchersProvider
import com.nathanfremont.beersapp.R
import com.nathanfremont.beersapp.common.CustomError
import com.nathanfremont.beersapp.common.NativeText
import com.nathanfremont.domain.Beer
import com.nathanfremont.domain.usecases.GetBeersMatchingFoodForPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val getBeersMatchingFoodForPageUseCase: GetBeersMatchingFoodForPageUseCase,
    private val dispatchersProvider: IDispatchersProvider,
) : ViewModel() {

    private var mutableIsLoading = MutableStateFlow(true)
    private var mutableError = MutableStateFlow<CustomError?>(null)
    private var mutableBeers = MutableStateFlow(emptyList<Beer>())
    private var mutableInputValue = MutableStateFlow<String?>(null)
    private var currentPage = 1

    val state = combine(
        mutableIsLoading,
        mutableError,
        mutableBeers,
    ) {
            isLoading: Boolean,
            error: CustomError?,
            beers: List<Beer>,
        ->
        BeerListState(
            isLoading = isLoading,
            error = error,
            beers = beers,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = BeerListState(
                isLoading = true,
                error = null,
                beers = emptyList(),
            )
        )

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            mutableInputValue
                .debounce(1_000)
                .collect { newInputValue: String? ->
                    currentPage = 1
                    getBeersMatchingFoodForPage(
                        matchingFood = newInputValue,
                        page = currentPage,
                    )
                }
        }
    }

    fun searchBeersMatchingFood(
        matchingFood: String?,
    ) {
        Timber.d("searchBeersMatchingFood ${"matchingFood" to matchingFood}")
        mutableInputValue.value = matchingFood
    }

    @Synchronized
    fun loadMoreBeers() {
        currentPage += 1
        Timber.d(
            "loadMoreBeers ${"matchingFood" to mutableInputValue.value}" +
                    "${"page" to currentPage}"
        )
        viewModelScope.launch(dispatchersProvider.io) {
            mutableIsLoading.value = true
            runCatching {
                val beers: List<Beer> =
                    getBeersMatchingFoodForPageUseCase.getBeersMatchingFoodForPage(
                        matchingFood = mutableInputValue.value,
                        page = currentPage,
                        perPage = 10,
                    )
                mutableBeers.value += beers
            }.getOrElse { error ->
                mutableError.value = CustomError(
                    errorTitle = NativeText.Resource(R.string.error_general_message_try_again),
                )
            }
            mutableIsLoading.value = false
        }
    }

    private fun getBeersMatchingFoodForPage(
        matchingFood: String?,
        page: Int,
        perPage: Int = 10,
    ) {
        Timber.d(
            "getBeersMatchingFoodForPage ${"matchingFood" to matchingFood}" +
                    "${"page" to page}" +
                    "${"perPage" to perPage}"
        )
        viewModelScope.launch(dispatchersProvider.io) {
            mutableIsLoading.value = true
            runCatching {
                val beers: List<Beer> =
                    getBeersMatchingFoodForPageUseCase.getBeersMatchingFoodForPage(
                        matchingFood = matchingFood,
                        page = page,
                        perPage = perPage,
                    )
                mutableBeers.value = beers
            }.getOrElse { error ->
                mutableError.value = CustomError(
                    errorTitle = NativeText.Resource(R.string.error_general_message_try_again),
                )
            }
            mutableIsLoading.value = false
        }
    }
}

data class BeerListState(
    val isLoading: Boolean,
    val error: CustomError?,
    val beers: List<Beer>,
)