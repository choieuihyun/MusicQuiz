package com.enjoy_project.musicquiz

import com.google.gson.annotations.SerializedName

data class UserTeam(

    @SerializedName("id")
    val id: Int?,


    @SerializedName("teamName")
    val teamName: String,


    @SerializedName("teamNumber")
    val teamNumber: Int,



)