package com.example.submission_1_intermediet.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.submission_1_intermediet.R
import com.example.submission_1_intermediet.databinding.ActivityLoginBinding
import com.example.submission_1_intermediet.ui.home.MainActivity
import com.example.submission_1_intermediet.ui.register.RegisterActivity
import com.google.android.material.animation.AnimatorSetCompat.playTogether
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.login)

        animation()
        init()
    }

    private fun init(){

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        loginViewModel.response.observe(this){
            checkUserExist(it)
        }

        with(binding){
            btnLogin.setOnClickListener {
                if(edtEmail.text?.let { isValidEmail(it.trim()) }!! && edtPassword.text?.trim()?.length!! > 5){
                    loginViewModel.setUserLogin(edtEmail.text!!.trim().toString(), edtPassword.text!!.trim().toString())
                }else{
                    if(!edtEmail.text?.let { isValidEmail(it.trim()) }!!) edtEmail.error = getString(R.string.edtEmailError)
                }
            }
            tvRegister.setOnClickListener {
                val intentToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intentToRegister)
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkUserExist(result : String){
        if(result == "success"){
            val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
            intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentToMain)
        }
        else{
            Toast.makeText(this@LoginActivity, getString(R.string.tvUserNotFound),Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun animation(){
        ObjectAnimator.ofFloat(binding.imgItemPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(login, password, email, register)
            start()
        }
    }
}