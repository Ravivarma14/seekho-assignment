package com.rvcn.seekhoanimeapplication.data.remote.model

data class AnimeCharactersResponse(
    val data: List<CharacterWrapperDto>
)

/*
data class CharacterWrapperDto(
    val character: CharacterDto
)

data class CharacterDto(
    val name: String,
    val images: ImageDto
)
*/
