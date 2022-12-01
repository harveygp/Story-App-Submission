package com.example.submission_1_intermediet.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem

class UserDiffCallBack(private val mOldUserList: List<ListStoryItem>, private val mNewUserList: List<ListStoryItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }
    override fun getNewListSize(): Int {
        return mNewUserList.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mNewUserList[newItemPosition]
        return oldEmployee.id == newEmployee.id && oldEmployee.name == newEmployee.name
    }
}