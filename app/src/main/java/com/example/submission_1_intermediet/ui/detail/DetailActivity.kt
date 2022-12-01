package com.example.submission_1_intermediet.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.submission_1_intermediet.R
import com.example.submission_1_intermediet.databinding.ActivityDetailBinding
import com.example.submission_1_intermediet.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.tvDetail)

        init()

    }

    private fun init(){
        val name = intent.getStringExtra(DATA_NAME) ?: ""
        val desc = intent.getStringExtra(DATA_DESCRIPTION) ?: ""
        val photo = intent.getStringExtra(DATA_PHOTO) ?: ""

        with(binding){
            Glide.with(this@DetailActivity)
                .load(photo)
                .into(imgPhoto)
            tvName.text = name
            tvDescription.text = desc
        }
    }

    companion object {
        const val DATA_NAME = "data_detail"
        const val DATA_DESCRIPTION = "data_desc"
        const val DATA_PHOTO = "data_photo"
    }
}