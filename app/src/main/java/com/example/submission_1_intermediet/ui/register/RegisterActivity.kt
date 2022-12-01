package com.example.submission_1_intermediet.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.submission_1_intermediet.R
import com.example.submission_1_intermediet.databinding.ActivityRegisterBinding
import com.example.submission_1_intermediet.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.register)

        init()
    }

    private fun init(){
        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }

        registerViewModel.responseRegister.observe(this){
            checkUserTaken(it)
        }

        with(binding){
            btnRegister.setOnClickListener {
                if( edtName.text?.trim()?.length!! > 0 && edtEmail.text?.let { isValidEmail(it.trim()) }!! && edtPassword.text?.trim()?.length!! > 5){
                    registerViewModel.setUserRegister(edtName.text?.trim().toString(),edtEmail.text!!.trim().toString(), edtPassword.text!!.trim().toString())
                }else{
                    if (edtName.text?.trim()?.isEmpty() == true) edtName.error = getString(R.string.edtNameError)
                    if(!edtEmail.text?.let { isValidEmail(it.trim()) }!!) edtEmail.error = getString(R.string.edtEmailError)
                    if(edtPassword.text?.trim()?.length!! < 6) edtPassword.error = getString(R.string.edtPasswordError)
                }
            }
        }
    }

    private fun checkUserTaken(result : Boolean){
        if(result){
            Toast.makeText(this@RegisterActivity, getString(R.string.tvEmailTaken), Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this@RegisterActivity, getString(R.string.tvEmailCreated), Toast.LENGTH_SHORT).show()
            val intentToLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            intentToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentToLogin)
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}