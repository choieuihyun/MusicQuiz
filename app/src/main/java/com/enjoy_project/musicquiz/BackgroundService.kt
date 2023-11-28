package com.enjoy_project.musicquiz

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.SortedMap


class BackgroundService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {

//        val teamName = params?.getStringExtra("userTeamName") ?: "defaultTeam"
        val teamName = params?.extras?.getString("userTeamName") ?: ""

        // 나머지 로직 수행
        fetchUserListFromServer(teamName) { userList ->
            // getUserCount 함수 호출
            getUserCount(userList as ArrayList<String>) { countHashMap ->
                // UI 업데이트 또는 다른 작업 수행
                // countHashMap을 사용하여 필요한 작업을 수행
                sendBroadcast(countHashMap)

                // 작업이 완료되면 작업을 종료한다고 시스템에 알림
                jobFinished(params, false)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun getUserCount(userList: ArrayList<String>, callback: (SortedMap<Int?, Int?>) -> Unit) {

        val countHashMap = LinkedHashMap<Int?, Int?>().toSortedMap(compareBy { it })

        userList.forEach { name ->

            RetrofitClient.userService.getUserByName(name).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    Log.d("BroadImplCount", "success")
                    Log.d("BroadImplUser", user.toString())
                    countHashMap[user!!.id] = user.count
                    // 요청 완료 후 콜백 반환
                    if (countHashMap.size == userList.size) {
                        callback(countHashMap)
                        Log.d("countList", countHashMap.toString())
                    }

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("BroadImplCount", "failed: ${t.message}")
                    callback(LinkedHashMap<Int?, Int?>().toSortedMap(compareBy { it }))
                }

            })

        }

    }

    private fun fetchUserListFromServer(teamName: String, callback: (List<String>?) -> Unit) {

        RetrofitClient.userService.getUserListByTeam(teamName).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    callback(userList)
                } else {
                    callback(null) // 실패 시 빈 목록 전달
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                callback(ArrayList()) // 실패 시 빈 목록 전달
            }
        })
    }

    private fun sendBroadcast(countHashMap: SortedMap<Int?, Int?>) {
        Log.d("BroadcastSender", "updateUI: Broadcasting point-updated")
        val broadcastIntent = Intent("point-updated")
        broadcastIntent.putExtra("countHashMap", countHashMap as Serializable)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}