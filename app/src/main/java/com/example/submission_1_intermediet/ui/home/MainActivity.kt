package com.example.submission_1_intermediet.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_1_intermediet.R
import com.example.submission_1_intermediet.adapter.ListStoriesAdapter
import com.example.submission_1_intermediet.adapter.LoadingStateAdapter
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.databinding.ActivityMainBinding
import com.example.submission_1_intermediet.ui.add.AddActivity
import com.example.submission_1_intermediet.ui.detail.DetailActivity
import com.example.submission_1_intermediet.ui.login.LoginActivity
import com.example.submission_1_intermediet.ui.map.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recycleUser: RecyclerView
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.tvDashboard)

        recycleUser = binding.rvUsers
        recycleUser.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recycleUser.layoutManager = layoutManager

        init()
    }

    private fun init(){
        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        setListUserData()
    }

    private fun setListUserData(){
        val adapter = ListStoriesAdapter()
        binding.rvUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.DATA_NAME, data.name)
                intentToDetail.putExtra(DetailActivity.DATA_DESCRIPTION, data.description)
                intentToDetail.putExtra(DetailActivity.DATA_PHOTO, data.photoUrl)
                startActivity(intentToDetail)
            }
        })


        lifecycleScope.launch {
            mainViewModel.changeLoadingState(false)
            mainViewModel.getData().collectLatest { pagingData ->
                adapter.submitData(pagingData) }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.logout -> {
                mainViewModel.setDeleteSesiandToken()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}