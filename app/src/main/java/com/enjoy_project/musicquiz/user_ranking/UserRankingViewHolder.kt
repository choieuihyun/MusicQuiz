package com.enjoy_project.musicquiz.user_ranking

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enjoy_project.musicquiz.R
import com.enjoy_project.musicquiz.User

class UserRankingViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val userNameTextView = view.findViewById<TextView>(R.id.userName)
    private val userCountTextView = view.findViewById<TextView>(R.id.userCount)
    private val userTeamTextView = view.findViewById<TextView>(R.id.userTeam)

    fun bind(user: User) {

        userNameTextView.text = user.name
        userCountTextView.text = user.count.toString()
        userTeamTextView.text = user.team

    }

}