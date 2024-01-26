package com.nathanfremont.domain

import com.nathanfremont.data.repository.BeerEntity
import com.nathanfremont.data.repository.BeersRepository
import com.nathanfremont.domain.usecases.GetBeersMatchingFoodForPageUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetBeersMatchingFoodForPageUseCaseTest {
    @MockK
    lateinit var beersRepository: BeersRepository

    @MockK
    lateinit var beerMapper: BeerMapper

    private lateinit var getBeersMatchingFoodForPageUseCase: GetBeersMatchingFoodForPageUseCase

    @BeforeEach
    fun setUp() {
        getBeersMatchingFoodForPageUseCase = GetBeersMatchingFoodForPageUseCase(
            beersRepository = beersRepository,
            beerMapper = beerMapper,
        )
    }

    @Test
    fun `getBeersMatchingFoodForPage - when no matching beers are found - returns empty list`() =
        runTest {
            val givenFlow = flowOf<List<BeerEntity>>(emptyList())
            coEvery {
                beersRepository.getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            } returns givenFlow

            coEvery {
                beerMapper.transformFromEntity(
                    entities = emptyList()
                )
            } returns emptyList<Beer>()

            val result = getBeersMatchingFoodForPageUseCase
                .getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            assert(result == emptyList<Beer>())
        }

    @Test
    fun `getBeersMatchingFoodForPage - when one matching beer is found - returns list of one item`() =
        runTest {
            val givenJsonList = listOf(
                BeerEntity(
                    name = "Tracie Doyle",
                    description = "brute",
                    imageUrl = "https://www.google.com/#q=pulvinar",
                )
            )
            val givenFlow = flowOf<List<BeerEntity>>(
                givenJsonList
            )
            coEvery {
                beersRepository.getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            } returns givenFlow

            val givenList = listOf(
                Beer(
                    name = "Tracie Doyle",
                    description = "brute",
                    imageUrl = "https://www.google.com/#q=pulvinar",
                )
            )
            coEvery {
                beerMapper.transformFromEntity(
                    entities = givenJsonList
                )
            } returns givenList

            val result = getBeersMatchingFoodForPageUseCase
                .getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            assert(result == givenList)
        }
}