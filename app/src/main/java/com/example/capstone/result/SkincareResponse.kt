package com.example.capstone.result

data class ProcessImageRequest(
    val image: String
)

data class ProcessImageResponse(
    val function: String,
    val name: String,
    val predicted_rating: String
)

data class SkincareResponse(
    val predicted_skincare_categories: List<String>,
    val results: List<ProcessImageResponse>
)