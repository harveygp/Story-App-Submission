package com.example.submission_1_intermediet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.databinding.ItemRowUsersBinding

class ListStoriesAdapter : PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding : ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : ListStoryItem){
            with(binding){
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .circleCrop()
                    .into(imgPhoto)
                tvName.text = data.name
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data!!)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(data)
        }

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(oldUser: ListStoryItem, newUser: ListStoryItem): Boolean {
                    return oldUser.id == newUser.id
                }

                override fun areContentsTheSame(oldUser: ListStoryItem, newUser: ListStoryItem): Boolean {
                    return oldUser == newUser
                }
            }
    }
}