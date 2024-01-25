package com.nathanfremont.domain.usecases

import com.nathanfremont.data.repository.BeersRepository
import com.nathanfremont.domain.BeerMapper
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBeersMatchingFoodForPageUseCase @Inject constructor(
    private val beersRepository: BeersRepository,
    private val beerMapper: BeerMapper,
) {
    suspend fun getBeersMatchingFoodForPage(
        matchingFood: String,
        page: Int,
        perPage: Int,
    ) = beersRepository
        .getBeersMatchingFoodForPage(
            matchingFood = matchingFood,
            page = page,
            perPage = perPage,
        )
        .map(beerMapper::transformFromEntity)
}