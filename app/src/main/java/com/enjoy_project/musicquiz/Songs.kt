package com.enjoy_project.musicquiz

import com.google.gson.annotations.SerializedName

data class Songs (

    @SerializedName("id")
    val id: Int?,

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("titleKr")
    val titleKr: String

        )