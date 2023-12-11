package com.enjoy_project.musicquiz.user_ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.enjoy_project.musicquiz.R
import com.enjoy_project.musicquiz.User

class UserRankingAdapter : ListAdapter<User, UserRankingViewHolder>(userRankingDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankingViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_ranking_list_item, parent, false)

        return UserRankingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserRankingViewHolder, position: Int) {

        val user = currentList[position]

        holder.bind(user)

    }


    companion object {

        private val userRankingDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }


}