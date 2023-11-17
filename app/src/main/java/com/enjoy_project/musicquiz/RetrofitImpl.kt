package com.enjoy_project.musicquiz

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RetrofitImpl {

    fun getList() {

        RetrofitClient.userService.userList()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e("retrofitImpl", response.toString())
                        return
                    } else {
                        val user = response.body()
                        Log.d("retrofitImplSuccess", user.toString())
                        response.body()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e("retrofitImpl", "연결 실패")
                    Log.e("retrofitImpl", t.toString())
                }

            })
    }

    fun getUser(id: Int, name: String, callback: (User?) -> Unit) {

        RetrofitClient.userService.userName(id, name)
            .enqueue(object : Callback<User> {

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    } else {
                        val user = response.body()
                        callback(user)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {

                    Log.e("retrofitImplName", t.toString())
                    callback(null)
                }

            })

    }

    suspend fun addUser(id: Int?, name: String, count: Int, team: String) {

        try {

            RetrofitClient.userService.addUser(id, name, count, team)
                .enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.isSuccessful) {
                            Log.d("retrofitImplPost", "success")
                        } else
                            Log.d("retrofitImplPost", response.message())
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Log.e("retrofitImplPost", "연결 실패")
                    }

                })

        }catch (e: NullPointerException) {
            Log.d("NPE", e.message!!)
        }


    }

    fun getSong(id: Int, callback: (Songs?) -> Unit) {

        RetrofitClient.userService.getSong(id).enqueue(object : Callback<Songs> {

            override fun onResponse(call: Call<Songs>, response: Response<Songs>) {
                if (response.isSuccessful.not()) {
                    return
                } else {
                    val songs = response.body()
                    callback(songs)
                }
            }

            override fun onFailure(call: Call<Songs>, t: Throwable) {
                callback(null)
            }


        })

    }



}