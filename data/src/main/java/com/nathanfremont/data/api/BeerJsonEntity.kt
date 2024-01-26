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
    @SerializedName("description")
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
)