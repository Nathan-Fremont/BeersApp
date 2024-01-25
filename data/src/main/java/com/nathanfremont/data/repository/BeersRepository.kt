package com.nathanfremont.data.repository

import com.nathanfremont.data.api.BeerRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeersRepository @Inject constructor(
    private val beerRemoteDataSource: BeerRemoteDataSource,
    private val beerEntityMapper: BeerEntityMapper,
) {
    fun getBeersMatchingFoodForPage(
        matchingFood: String,
        page: Int,
        perPage: Int,
    ) : Flow<List<BeerEntity>> = beerRemoteDataSource
        .getBeersMatchingFoodForPage(
            matchingFood = matchingFood,
            page = page,
            perPage = perPage,
        )
        .map(beerEntityMapper::transformJsonToEntity)
}