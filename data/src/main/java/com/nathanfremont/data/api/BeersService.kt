package com.nathanfremont.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeersService {
    @GET("beers")
    suspend fun getBeersMatchingFoodForPage(
        @Query("food")
        food: String,
        @Query("food")
        page: Int,
        @Query("perPage")
        perPage: Int = 25,
    ): Response<List<BeerJsonEntity>>
}