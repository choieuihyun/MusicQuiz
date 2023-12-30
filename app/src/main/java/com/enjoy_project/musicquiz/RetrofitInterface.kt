package com.enjoy_project.musicquiz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RetrofitInterface {

    @GET("user/all")
    fun userList(): Call<List<User>>

    @GET("user")
    fun getUserByName(@Query("name") name: String): Call<User>

    @GET("user/team")
    fun getUserListByTeam(@Query("team") team: String): Call<List<String>>

    @GET("user/")
    fun getUserTeam(@Query("teamName") teamName: String, @Query("teamNumber") teamNumber: Int): Call<UserTeam>

    // POST는 리턴값 없어서 설정 안했더니 Void 타입으로 리턴 해야된대.
    @POST("user/")
    suspend fun addUser(@Query("id") id: Int?, @Query("name") name: String, @Query("count") count: Int, @Query("team") team: String): Call<Void?>

    @POST("userTeam/")
    suspend fun addUserTeam(@Query("id") id: Int?, @Query("team_name") teamName: String, @Query("teamNumber") teamNumber: Int?): Call<Void?>

    @POST("userCount/")
    suspend fun addUserCount(@Query("name") name: String, @Query("team") team: String): Call<Void?>

    @GET("songs/")
    fun getSong(@Query("id") id: Int): Call<Songs>

    @GET("songsChild/")
    fun getSongChild(@Query("id") id: Int): Call<SongsChild>

}