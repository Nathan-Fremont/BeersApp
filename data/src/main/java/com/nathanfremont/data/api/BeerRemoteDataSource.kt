package com.nathanfremont.data.api

import com.google.gson.Gson
import com.nathanfremont.data.logd
import com.nathanfremont.data.retryOnInternalErrorServerHttpCodes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named

class BeerRemoteDataSource @Inject constructor(
    @Named("baseBeersApiUrl")
    baseApiUrl: String,
    baseOkHttpClient: OkHttpClient,
) {
    private val service: BeersService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(baseApiUrl)
        .client(baseOkHttpClient)
        .build()
        .create(BeersService::class.java)

    fun getBeersMatchingFoodForPage(
        matchingFood: String?,
        page: Int,
        perPage: Int,
    ): Flow<List<BeerJsonEntity>> = callbackFlow {
        val beers = service.getBeersMatchingFoodForPage(
            food = matchingFood,
            page = page,
            perPage = perPage,
        )
        trySend(beers)
        close()
        awaitClose()
    }
        .retryOnInternalErrorServerHttpCodes()
        .logd(
            tag = "BeersRemoteDataSource",
            method = "getBeersMatchingFoodForPage",
            "matchingFood" to matchingFood,
            "page" to page,
            "perPage" to perPage,
        )
        .filterNotNull()
}