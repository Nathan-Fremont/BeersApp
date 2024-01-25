package com.nathanfremont.domain

import com.nathanfremont.data.api.BeerJsonEntity
import com.nathanfremont.data.repository.BeerEntity
import javax.inject.Inject

class BeerMapper @Inject constructor() {
    fun transformFromEntity(
        entities: List<BeerEntity>,
    ): List<Beer> = entities.map(::transformFromEntity)

    fun transformFromEntity(
        entity: BeerEntity,
    ): Beer = with(entity) {
        Beer(
            name = name,
            description = description,
            imageUrl = imageUrl,
            foodPairing = foodPairing,
        )
    }
}