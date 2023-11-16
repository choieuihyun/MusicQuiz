package com.enjoy_project.musicquiz

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id")
    val id: Int?,


    @SerializedName("name")
    val name: String,


    @SerializedName("count")
    val count: Int,


    @SerializedName("team")
    val team: String


)