package com.example.poetpal.network.mirriamWebsterApi

import kotlinx.serialization.Serializable

@Serializable
data class ApiWebsterEntry(
    val hwi: Hwi,
)

@Serializable
data class Hwi(
    val hw: String,
)
