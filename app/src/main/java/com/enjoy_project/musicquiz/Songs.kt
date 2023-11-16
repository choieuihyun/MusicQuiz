package com.enjoy_project.musicquiz

import com.google.gson.annotations.SerializedName

data class Songs (

    @SerializedName("id")
    val id: Int?,

    @SerializedName("title")
    val title: String,

    @SerializedName("question1")
    val question1: String,

    @SerializedName("question2")
    val question2: String,

    @SerializedName("question3")
    val question3: String,

    @SerializedName("question4")
    val question4: String,

    @SerializedName("question5")
    val question5: String,

    )