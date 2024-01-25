package com.nathanfremont.data.api

import android.health.connect.datatypes.units.Volume
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Method

@Keep
data class BeerJsonEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("first_brewed")
    val firstBrewed: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("abv")
    val abv: Double,
    @SerializedName("ibu")
    val ibu: Double,
    @SerializedName("target_fg")
    val targetFg: Double,
    @SerializedName("target_og")
    val targetOg: Double,
    @SerializedName("ebc")
    val ebc: Double,
    @SerializedName("srm")
    val srm: Double,
    @SerializedName("ph")
    val ph: Double,
    @SerializedName("attenuation_level")
    val attenuationLevel: Double,
    @SerializedName("volume")
    val volume: Volume,
    @SerializedName("boil_volume")
    val boilVolume: Volume,
    @SerializedName("method")
    val method: Method,
    @SerializedName("ingredients")
    val ingredients: Ingredients,
    @SerializedName("food_pairing")
    val foodPairing: List<String>,
    @SerializedName("brewers_tips")
    val brewersTips: String,
    @SerializedName("contributed_by")
    val contributedBy: String
)

@Keep
data class Volume(
    @SerializedName("value")
    val value: Int,
    @SerializedName("unit")
    val unit: String
)

@Keep
data class Method(
    @SerializedName("mash_temp")
    val mashTemp: List<MashTemp>,
    @SerializedName("fermentation")
    val fermentation: Fermentation,
    @SerializedName("twist")
    val twist: Any
)

@Keep
data class Ingredients(
    @SerializedName("malt")
    val malt: List<Ingredient>,
    @SerializedName("hops")
    val hops: List<Ingredient>,
    @SerializedName("yeast")
    val yeast: String
)

@Keep
data class Ingredient(
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: Amount
)

@Keep
data class Amount(
    @SerializedName("value")
    val value: Double,
    @SerializedName("unit")
    val unit: String
)

@Keep
data class Fermentation(
    @SerializedName("temp")
    val temp: Temp
)

@Keep
data class Temp(
    @SerializedName("value")
    val value: Int,
    @SerializedName("unit")
    val unit: String
)

@Keep
data class MashTemp(
    @SerializedName("temp")
    val temp: Temp,
    @SerializedName("duration")
    val duration: Int
)