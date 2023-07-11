package com.example.carsharingv2.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.carsharingv2.models.UtenteModel
import com.example.carsharingv2.controllers.DBManager
import com.example.carsharingv2.controllers.QueryCallback
import com.example.carsharingv2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.xSignUpButton.setOnClickListener {
            val user_email = binding.xEmail.text.toString().trim()
            val user_pass = binding.xPassword.text.toString().trim()
            val user_pass2 = binding.xPasswordRepeat.text.toString().trim()
            val user_name = binding.xNome.text.toString().trim()
            val user_cognome = binding.xCognome.text.toString().trim()

            //controlla i campi email e pass e se le due pass coincidono
            if(user_email.isEmpty()) {
                binding.xEmail.error = "Email required"
                binding.xEmail.requestFocus()
                return@setOnClickListener
            }
            if(user_pass.isEmpty()) {
                binding.xPassword.error = "Password required"
                binding.xPassword.requestFocus()
                return@setOnClickListener
            }
            if(user_pass2.isEmpty()) {
                binding.xPasswordRepeat.error = "Repeat Password required"
                binding.xPasswordRepeat.requestFocus()
                return@setOnClickListener
            }

            val utente = UtenteModel(null, user_email, user_pass, user_name, user_cognome, null)

            if (user_pass.equals(user_pass2)) {

                registrazione(utente)

            } else { //password non coincidono
                binding.xPassword.error = "Password must be the same"
                binding.xPassword.requestFocus()
                return@setOnClickListener
            }
        }
    }

    fun startmainpage() {
        Log.i("SignUpActivity", "Intent")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    //manda un insert al db
    fun registrazione(utenteModel: UtenteModel) {
        DBManager().signup(utenteModel, object : QueryCallback {
            override fun success(response: Any, success: String) {
                startmainpage()
                Toast.makeText(this@SignUpActivity, "AGGIUNTO CORRETTAMENTE", Toast.LENGTH_SHORT).show()
            }

            override fun failure(fail: String) {
                Toast.makeText(this@SignUpActivity, fail, Toast.LENGTH_SHORT).show()
            }
        })
    }
}