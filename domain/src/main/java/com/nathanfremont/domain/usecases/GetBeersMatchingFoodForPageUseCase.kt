package com.nathanfremont.domain.usecases

import com.nathanfremont.data.CustomTimeoutException
import com.nathanfremont.data.repository.BeersRepository
import com.nathanfremont.data.withTimeout
import com.nathanfremont.domain.Beer
import com.nathanfremont.domain.BeerMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBeersMatchingFoodForPageUseCase @Inject constructor(
    private val beersRepository: BeersRepository,
    private val beerMapper: BeerMapper,
) {
    @Throws(CustomTimeoutException::class)
    suspend fun getBeersMatchingFoodForPage(
        matchingFood: String?,
        page: Int,
        perPage: Int,
    ): List<Beer> = beersRepository
        .getBeersMatchingFoodForPage(
            matchingFood = matchingFood,
            page = page,
            perPage = perPage,
        )
        .map(beerMapper::transformFromEntity)
        .withTimeout(5_000L)
}