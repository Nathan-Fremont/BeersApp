package com.nathanfremont.data.repository

import com.nathanfremont.data.api.BeerJsonEntity
import javax.inject.Inject

class BeerEntityMapper @Inject constructor() {
    fun transformJsonToEntity(
        jsonEntities: List<BeerJsonEntity>,
    ): List<BeerEntity> = jsonEntities.map(::transformJsonToEntity)

    fun transformJsonToEntity(
        jsonEntity: BeerJsonEntity,
    ): BeerEntity = with(jsonEntity) {
        BeerEntity(
            name = name,
            description = description,
            imageUrl = imageUrl,
        )
    }
}