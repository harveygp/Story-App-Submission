package com.example.submission_1_intermediet.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @field:SerializedName("error")
    val error : Boolean? = null,

    @field:SerializedName("message")
    val message : String? = null
)
