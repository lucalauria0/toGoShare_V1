package com.example.carsharingv2.start

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.carsharingv2.main.StartActivity
import com.example.carsharingv2.models.UtenteModel
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("mieprefrenze", Context.MODE_PRIVATE)
        binding.xLoginNick.setText(sharedPreferences.getString("email", ""))
        binding.xLoginPassword.setText(sharedPreferences.getString("password", ""))
        binding.ricordami.isChecked = sharedPreferences.getBoolean("check", false)

        binding.ricordami.setOnCheckedChangeListener()
        { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("check", isChecked)
            editor.apply()
        }

        binding.xSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //controlla campi email e pass
        binding.xLoginButton.setOnClickListener {

            val user_email = binding.xLoginNick.text.toString().trim()
            val user_pass = binding.xLoginPassword.text.toString().trim()

            if(user_email.isEmpty()) {
                binding.xLoginNick.error = "Email required"
                binding.xLoginNick.requestFocus()
                return@setOnClickListener
            }
            if(user_pass.isEmpty()) {
                binding.xLoginPassword.error = "Password required"
                binding.xLoginPassword.requestFocus()
                return@setOnClickListener
            }

            verificaCredenziali(user_email, user_pass)

        }
    }

    //verifica se utente e' presente nel DB
    fun verificaCredenziali(email: String, password: String)
    {
        DBManager().getUtente(this, email, password, object :
            QueryCallback {

            override fun success(response: Any, success: String) {
                val intent = Intent(this@MainActivity, StartActivity::class.java)
                intent.putExtra("utente", response as UtenteModel)
                startActivity(intent)
            }

            override fun failure(fail: String) {
                Toast.makeText(this@MainActivity, "FAIL "+fail, Toast.LENGTH_SHORT).show()
            }
            } )
    }

}