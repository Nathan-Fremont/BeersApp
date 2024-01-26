package com.nathanfremont.beersapp.common

import java.util.UUID

data class CustomError(
    private val uuid: UUID = UUID.randomUUID(),
    val errorTitle: NativeText,
)