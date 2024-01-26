package com.nathanfremont.beersapp

import com.nathanfremont.beersapp.beerlist.BeerListState
import com.nathanfremont.beersapp.beerlist.BeerListViewModel
import com.nathanfremont.coretest.CoroutineTestExtension
import com.nathanfremont.domain.Beer
import com.nathanfremont.domain.usecases.GetBeersMatchingFoodForPageUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineTestExtension::class)
class BeerListViewModelTest {
    @MockK
    lateinit var getBeersMatchingFoodForPageUseCase: GetBeersMatchingFoodForPageUseCase

    private lateinit var beerListViewModel: BeerListViewModel

    @BeforeEach
    fun setUp() {
        beerListViewModel = BeerListViewModel(
            getBeersMatchingFoodForPageUseCase = getBeersMatchingFoodForPageUseCase,
            dispatchersProvider = TestDispatchersProvider(),
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchBeersMatchingFood - when no matching beers are found - returns empty list`() =
        runTest {
            coEvery {
                getBeersMatchingFoodForPageUseCase.getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 10,
                )
            } returns emptyList()

            assert(
                beerListViewModel.state.value == BeerListState(
                    isLoading = true,
                    error = null,
                    beers = emptyList(),
                )
            )
            beerListViewModel
                .searchBeersMatchingFood(
                    matchingFood = null,
                )
            advanceTimeBy(10_000)
            val resultState = beerListViewModel.state.value
            assert(
                resultState == BeerListState(
                    isLoading = false,
                    error = null,
                    beers = emptyList(),
                )
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchBeersMatchingFood - when one matching beer is found - returns list of one beer`() =
        runTest {
            val givenBeers = listOf(
                Beer(
                    name = "Sonny Gentry",
                    description = "deseruisse",
                    imageUrl = "https://www.google.com/#q=nonumes",
                )
            )
            coEvery {
                getBeersMatchingFoodForPageUseCase.getBeersMatchingFoodForPage(
                    matchingFood = "truc",
                    page = 1,
                    perPage = 10,
                )
            } returns givenBeers

            assert(
                beerListViewModel.state.value == BeerListState(
                    isLoading = true,
                    error = null,
                    beers = emptyList(),
                )
            )
            beerListViewModel
                .searchBeersMatchingFood(
                    matchingFood = "truc",
                )
            advanceTimeBy(10_000)
            val resultState = beerListViewModel.state.value
            assert(
                resultState == BeerListState(
                    isLoading = false,
                    error = null,
                    beers = givenBeers,
                )
            )
        }
}