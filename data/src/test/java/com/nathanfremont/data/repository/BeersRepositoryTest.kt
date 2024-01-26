package com.nathanfremont.data.repository

import app.cash.turbine.test
import com.nathanfremont.data.api.BeerJsonEntity
import com.nathanfremont.data.api.BeerRemoteDataSource
import com.nathanfremont.data.repository.BeerEntity
import com.nathanfremont.data.repository.BeerEntityMapper
import com.nathanfremont.data.repository.BeersRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BeersRepositoryTest {
    @MockK
    lateinit var beerRemoteDataSource: BeerRemoteDataSource

    @MockK
    lateinit var beerEntityMapper: BeerEntityMapper

    private lateinit var beersRepository: BeersRepository

    @BeforeEach
    fun setUp() {
        beersRepository = BeersRepository(
            beerRemoteDataSource = beerRemoteDataSource,
            beerEntityMapper = beerEntityMapper,
        )
    }

    @Test
    fun `getBeersMatchingFoodForPage - when no matching beers are found - returns empty list and complete`() =
        runTest {
            val givenFlow = flowOf<List<BeerJsonEntity>>(emptyList())
            coEvery {
                beerRemoteDataSource.getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            } returns givenFlow

            coEvery {
                beerEntityMapper.transformJsonToEntity(
                    jsonEntities = emptyList()
                )
            } returns emptyList<BeerEntity>()

            val resultFlow = beersRepository.getBeersMatchingFoodForPage(
                matchingFood = null,
                page = 1,
                perPage = 25,
            )
            resultFlow.test {
                assert(
                    awaitItem() == emptyList<BeerEntity>()
                )
                awaitComplete()
            }
        }

    @Test
    fun `getBeersMatchingFoodForPage - when one matching beer is found - returns list of one item and complete`() =
        runTest {
            val givenJsonList = listOf(
                BeerJsonEntity(
                    id = 2315,
                    name = "Tracie Doyle",
                    description = "brute",
                    imageUrl = "https://www.google.com/#q=pulvinar",
                )
            )
            val givenFlow = flowOf<List<BeerJsonEntity>>(
                givenJsonList
            )
            coEvery {
                beerRemoteDataSource.getBeersMatchingFoodForPage(
                    matchingFood = null,
                    page = 1,
                    perPage = 25,
                )
            } returns givenFlow

            val givenList = listOf(
                BeerEntity(
                    name = "Tracie Doyle",
                    description = "brute",
                    imageUrl = "https://www.google.com/#q=pulvinar",
                )
            )
            coEvery {
                beerEntityMapper.transformJsonToEntity(
                    jsonEntities = givenJsonList
                )
            } returns givenList

            val resultFlow = beersRepository.getBeersMatchingFoodForPage(
                matchingFood = null,
                page = 1,
                perPage = 25,
            )
            resultFlow.test {
                assert(
                    awaitItem() == givenList
                )
                awaitComplete()
            }
        }
}