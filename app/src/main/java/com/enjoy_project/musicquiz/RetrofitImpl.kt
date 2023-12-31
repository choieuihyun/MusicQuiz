package com.enjoy_project.musicquiz

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.SortedMap


// 함수명 작명 절망적이네 ㅋㅋ
class RetrofitImpl {

    // 유저 전체 리스트 불러오기
    fun getList(callback: (List<User>?) -> Unit) {

        RetrofitClient.userService.userList()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e("retrofitImpl", response.toString())
                        callback(null)
                    } else {
                        val user = response.body()
                        callback(user)
                        Log.d("retrofitImplSuccess", user.toString())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e("retrofitImpl", "연결 실패")
                    Log.e("retrofitImpl", t.toString())
                }

            })
    }

    // 랭킹 리스트를 위해 count 기준으로 내림차로 유저를 불러오는 코드
    fun getListDescCount(callback: (List<User>?) -> Unit) {

        RetrofitClient.userService.userList()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e("retrofitImpl", response.toString())
                        callback(null)
                    } else {
                        val user = response.body()
                        val sortedUserList = user?.sortedWith(compareByDescending { it.count })
                        callback(sortedUserList)
                        Log.d("retrofitImplSuccess", user.toString())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Log.e("retrofitImpl", "연결 실패")
                    Log.e("retrofitImpl", t.toString())
                }

            })
    }


    fun getUserListByTeam(team: String, callback: (List<String>?) -> Unit) {

        RetrofitClient.userService.getUserListByTeam(team)
            .enqueue(object : Callback<List<String>> {

                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e("ImplByTeamFailed", response.toString())
                        callback(null)
                    } else {
                        val user = response.body()
                        callback(user)
                        Log.d("ImplByTeamSuccess", user.toString())
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.e("retrofitImplByTeam", "연결 실패")
                    Log.e("retrofitImplByTeam", t.toString())
                }

            })
    }

    fun getUser(name: String, callback: (User?) -> Unit) {

        RetrofitClient.userService.getUserByName(name)
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

    fun getUserTeam(teamName: String, teamNumber: Int) {

        try {

            RetrofitClient.userService.getUserTeam(teamName, teamNumber)
                .enqueue(object : Callback<UserTeam> {
                    override fun onResponse(call: Call<UserTeam>, response: Response<UserTeam>) {
                        if (response.isSuccessful) {
                            Log.d("retrofitImplPost", "success")
                        } else
                            Log.d("retrofitImplPost", response.message())
                    }

                    override fun onFailure(call: Call<UserTeam?>, t: Throwable) {
                        Log.e("retrofitImplPost", "연결 실패")
                    }

                })

        } catch (e: NullPointerException) {
            Log.d("NPE", e.message!!)
        }

    }

    fun getUserCount(userList: ArrayList<String>, callback: (SortedMap<Int?,Int?>) -> Unit) {

        val countHashMap = LinkedHashMap<Int?, Int?>().toSortedMap(compareBy { it })

        userList.forEach { name ->

            RetrofitClient.userService.getUserByName(name).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    Log.d("retrofitImplCount", "success")
                    Log.d("retrofitImplUser", user.toString())
                    countHashMap[user!!.id] = user.count
                    // 요청 완료 후 콜백 반환
                    if (countHashMap.size == userList.size) {
                        callback(countHashMap)
                        Log.d("countList", countHashMap.toString())
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("retrofitImplCount", "failed: ${t.message}")
                    callback(LinkedHashMap<Int?, Int?>().toSortedMap(compareBy { it }))
                }

            })

        }

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

        } catch (e: NullPointerException) {
            Log.d("NPE", e.message!!)
        }
    }

    suspend fun addUserTeam(id: Int?, teamName: String, teamNumber: Int?) {

        try {

            RetrofitClient.userService.addUserTeam(id, teamName, teamNumber)
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

        } catch (e: NullPointerException) {
            Log.d("NPE", e.message!!)
        }
    }

    suspend fun addUserCount(name: String, team: String) {

        try {

            RetrofitClient.userService.addUserCount(name, team)
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

        } catch (e: NullPointerException) {
            Log.d("NPE", e.message!!)
        }

    }

    fun getSong(id: Int, callback: (Songs?) -> Unit) {

        RetrofitClient.userService.getSong(id).enqueue(object : Callback<Songs> {

            override fun onResponse(call: Call<Songs>, response: Response<Songs>) {
                if (response.isSuccessful) {
                    val songs = response.body()
                    callback(songs)
                }
            }

            override fun onFailure(call: Call<Songs>, t: Throwable) {

            }

        })

    }


    // 노래 세부 사항들(이름 바꿔야 할 듯)
    fun getSongChild(id: Int, callback: (SongsChild?) -> Unit) {

        RetrofitClient.userService.getSongChild(id).enqueue(object : Callback<SongsChild> {

            override fun onResponse(call: Call<SongsChild>, response: Response<SongsChild>) {
                if (response.isSuccessful.not()) {
                    return
                } else {
                    val songsChild = response.body()
                    callback(songsChild)
                }
            }

            override fun onFailure(call: Call<SongsChild>, t: Throwable) {
                callback(null)
            }


        })

    }


}


