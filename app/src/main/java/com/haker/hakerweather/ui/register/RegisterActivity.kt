package com.haker.hakerweather.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.haker.hakerweather.data.model.User
import com.haker.hakerweather.databinding.ActivityRegisterBinding
import com.haker.hakerweather.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        if (isValidate()) {
            Log.i(TAG, "checkUserExistsOrNot = ${checkUserExistsOrNot()}")
            if (!checkUserExistsOrNot()) {
                val user =
                    User(binding.tieEmail.text.toString(), binding.tiePwd.text.toString(), "Email")
                    viewModel.saveUser(user)
                    Toast.makeText(this, "User Register Successfully!", Toast.LENGTH_SHORT).show()
                    goToLogin()
            }
            else {
                Toast.makeText(this, "Email Already Existed!", Toast.LENGTH_SHORT).show()
                binding.tieEmail.setText("")
                binding.tieEmail.requestFocus()
            }
        }
    }

    private fun isValidate(): Boolean {
        if (binding.tieEmail.length() == 0) {
            binding.tieEmail.error = "Please enter email"
            binding.tieEmail.requestFocus()
            return false
        }
        if (binding.tiePwd.length() == 0) {
            binding.tiePwd.error = "Please enter password"
            binding.tiePwd.requestFocus()
            return false
        }
        if (binding.tieConfirmPwd.length() == 0) {
            binding.tieConfirmPwd.error = "Please enter confirm password"
            binding.tieConfirmPwd.requestFocus()
            return false
        }
        if (binding.tiePwd.text.toString() != binding.tieConfirmPwd.text.toString()) {
            binding.tieConfirmPwd.error = "Passwords must be the same!"
            binding.tieConfirmPwd.requestFocus()
            return false
        }
        return true
    }

    private fun checkUserExistsOrNot(): Boolean {
        var isExist = true
        viewModel.getUserByEmail(binding.tieEmail.text.toString()).observe(this, Observer {
            if (it.isNotEmpty()) {
                isExist = true
                Log.i(TAG, "isExist_if = $isExist")
            }
            else {
                isExist = false
            }
        })
        Log.i(TAG, "isExist = $isExist")
        return isExist
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToLogin()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}