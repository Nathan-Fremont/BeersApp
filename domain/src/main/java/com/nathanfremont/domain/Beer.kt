package com.nathanfremont.domain

data class Beer(
    val name: String,
    val description: String,
    val imageUrl: String,
    val foodPairing: List<String>,
)