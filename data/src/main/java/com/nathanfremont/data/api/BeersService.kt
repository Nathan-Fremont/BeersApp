package com.nathanfremont.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeersService {
    @GET("beers")
    suspend fun getBeersMatchingFoodForPage(
        @Query("food")
        food: String?,
        @Query("page")
        page: Int,
        @Query("per_page")
        perPage: Int = 25,
    ): Response<List<BeerJsonEntity>>
}