package com.haker.hakerweather.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.haker.hakerweather.R
import com.haker.hakerweather.databinding.ActivityLoginBinding
import com.haker.hakerweather.ui.main.MainActivity
import com.haker.hakerweather.ui.register.RegisterActivity
import com.haker.hakerweather.ui.register.RegisterViewModel
import com.haker.hakerweather.util.SavedPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val REQUEST_CODE: Int = 123
    var firebaseAuth = FirebaseAuth.getInstance()

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signInWithGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.register.setOnClickListener {
            goToRegister()
        }

        binding.btnLogin.setOnClickListener {
            if (isValidate()) {
                viewModel.login(binding.tieEmail.text.toString(), binding.tiePwd.text.toString()).observe(this, Observer {
                    Log.i(TAG, "users_size = ${it.size}")
                    if (it.isNotEmpty()) {
                        Toast.makeText(applicationContext, "Login successfully!", Toast.LENGTH_LONG).show()
                        SavedPreference.setEmail(applicationContext, binding.tieEmail.text.toString())
                        SavedPreference.setHasLogin(applicationContext, "yes")
                        goToMain()
                    }
                    else {
                        Toast.makeText(applicationContext, "Email or Password is wrong!", Toast.LENGTH_LONG).show()
                    }
                })
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
        return true
    }

    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            Log.i(TAG, e.toString())
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                Toast.makeText(applicationContext, "Sign in with Google successfully!", Toast.LENGTH_LONG).show()
                goToMain()
                Log.i(TAG, "Email = ${account.email}")
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else if(SavedPreference.getHasLogin(this) != null) {
            if (SavedPreference.getHasLogin(this).toString() == "yes") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}