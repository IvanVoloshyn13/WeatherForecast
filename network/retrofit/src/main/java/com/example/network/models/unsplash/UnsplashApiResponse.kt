package com.example.network.models.unsplash

import com.squareup.moshi.Json

data class UnsplashApiResponse(
    @Json(name = "results")
    val imageList: List<UnsplashImageResult>,
    @Json(name = "total_pages")
    val totalPages: Int
)