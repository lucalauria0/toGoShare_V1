package com.example.carsharingv2.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.carsharingv2.main.fragment.AccountFragment
import com.example.carsharingv2.main.fragment.AddAutoFragment
import com.example.carsharingv2.main.fragment.HomeFragment
import com.example.carsharingv2.R
import com.example.carsharingv2.databinding.ActivityStartBinding
import com.example.carsharingv2.models.UtenteModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding
    lateinit var bottomNav : BottomNavigationView
    lateinit var utente : UtenteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        utente = intent.extras!!.getParcelable("utente")!! //dichiara utente come utente model e poi settalo dentro un bundle
        var bundle = Bundle()
        bundle.putParcelable("utente", utente)

        loadFragment(HomeFragment(), bundle)
        bottomNav = findViewById(R.id.xBottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment(), bundle)
                    true
                }
                R.id.add -> {
                    loadFragment(AddAutoFragment(), bundle)
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment(), bundle)
                    true
                }
                else -> {
                    false
                }
            }
        }


    }

    fun loadFragment(fragment: Fragment, bundle: Bundle?){
        val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_left_to_right,
            R.anim.enter_right_to_left
        )
        transaction.replace(R.id.xContainer,fragment)
        fragment.arguments = bundle
        transaction.commit()
    }
}